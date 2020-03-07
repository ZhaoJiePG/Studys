package com.zj.source

import org.apache.flink.streaming.api.scala._

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 从集合和文件中读取数据
 */
object fileSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //从集合读取数据
    val sensorLists = List(
      SensorReading("sensor_1", 1547718199, 35.80018327300259),
      SensorReading("sensor_6", 1547718201, 15.402984393403084),
      SensorReading("sensor_7", 1547718202, 6.720945201171228),
      SensorReading("sensor_10", 1547718205, 38.101067604893444)
    )

    //从文件读取数据
    val stream: DataStream[String] = env.readTextFile("YOUR_FILE_PATH")

    val stream1: DataStream[SensorReading] = env.fromCollection(sensorLists)
    //转换成元祖
    val stream2: DataStream[(String, Double, Long)] = stream1.map(item => (item.id,item.temperature,item.timestamp))

    //从任意集合
    env.fromElements(1,2.0,"String").print()

    //输出
    stream2.print("stream2:").setParallelism(1)
    env.execute("fileSource")
  }
}

//温度传感器
case class SensorReading(id: String, timestamp: Long, temperature: Double)