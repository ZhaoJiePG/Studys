package Function

import FlinkLearing.Transform.SensorReading
import org.apache.flink.api.common.functions.{FilterFunction, RichMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * Created by ZJ on 2020-1-7
 * comment:
 */
object FunctionApi {
  //温度传感器
  case class SensorReading(id: String, timestamp: Long, temperature: Double)
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //读取数据
    val stream1: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")

    val stream2: DataStream[SensorReading] = stream1.map(data => {
      val dataList: Array[String] = data.split(",")
      val sensorReading: SensorReading = SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toLong)
      sensorReading
    })

    //函数类
//    stream2.filter(new myFilter()).print()

    //匿名函数
    stream2.filter(_.temperature>4).print()

    //富函数

    env.execute("")
  }

}

class myFilter extends FilterFunction[SensorReading]{
  override def filter(t: SensorReading): Boolean = {
    if(t.temperature>4) true else false
  }
}

class  myMapper extends RichMapFunction[SensorReading,String]{
  override def map(in: SensorReading): String = {
    1.toString
  }

  override def open(parameters: Configuration): Unit = super.open(parameters)

  override def close(): Unit = super.close()
}