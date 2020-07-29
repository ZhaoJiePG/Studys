package com.yadea.source

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 以 kafka 消息队列的数据作为来源
 */
object KafkaSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //定义属性
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "kafka02:9092,kafka03:9092,kafka04:9092")
    properties.setProperty("group.id", "group01")
    properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "latest")

    //添加kafkaSource
    val stream: DataStream[String] = env.addSource(new FlinkKafkaConsumer011[String]("test02",new SimpleStringSchema(),properties))

    stream.setParallelism(1).print("kafkaSource:")
    env.execute()
  }
}
