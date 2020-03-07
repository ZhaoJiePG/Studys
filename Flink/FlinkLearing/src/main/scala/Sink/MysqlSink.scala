package FlinkLearing.Sink

import java.sql.{Connection, Driver, DriverManager, PreparedStatement}

import FlinkLearing.Sink.KafkaSink.SensorReading
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._

/**
 * Created by ZJ on 2020-2-17
 * comment:
 */
object MysqlSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //transform操作
    val inputStream: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      //将对象转换位string
      SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).toDouble)
    })

    dataStream.addSink(new MyJdbcSink)

    env.execute("transform test job")
  }
}

class MyJdbcSink() extends RichSinkFunction[SensorReading]{
  //定义初Mysql JDBC链接、定义预编译器
  var conn: Connection = _
  var insertStmt: PreparedStatement = _
  var updateStmt:PreparedStatement = _

  //初始化，创建链接和预编译语句
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    conn = DriverManager.getConnection("jdbc:mysql://10.149.1.154:3306/test","root","root")
    insertStmt = conn.prepareStatement("INSERT INTO temporary (sensor,temp) VALUES (?,?)")
    updateStmt = conn.prepareStatement("UPDATE temporary SET temp = ? WHERE sensor = ?")
  }

  //调用链接执行sql
  override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
    //执行更新语句
    updateStmt.setDouble(1,value.temperature)
    updateStmt.setString(2,value.id)
    updateStmt.execute()
    //如果没用查到数据执行插入
    if(updateStmt.getUpdateCount == 0){
      insertStmt.setString(1,value.id)
      insertStmt.setDouble(2,value.temperature)
      insertStmt.execute()
    }
  }

  //关闭链接
  override def close(): Unit = {
    insertStmt.close()
    updateStmt.close()
    conn.close()
  }
}
