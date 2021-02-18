package SparkStreaming

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

/**
  * Created by ZJ on 2021/2/6
  * comment:
  */
object KafkaDirectStreaming {
  // TODO 创建环境对象
  // StreamingContext创建时，需要传递两个参数
  // 第一个参数表示环境配置
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
  // 第二个参数表示批量处理的周期（采集周期）
  val ssc = new StreamingContext(sparkConf, Seconds(3))

  val kafkaPara: Map[String, Object] = Map[String, Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "linux1:9092,linux2:9092,linux3:9092",
    ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
  )

  val kafkaDataDS: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
    ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Set("atguiguNew"), kafkaPara)
  )
  kafkaDataDS.map(_.value()).print()


  ssc.awaitTermination()
}
