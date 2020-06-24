package Sql

import FlinkLearing.Transform.SensorReading
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * Created by ZJ on 2020-2-17
 * comment:
 */
object TableApi {
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

    dataStream.print()
    env.execute()
  }
}
