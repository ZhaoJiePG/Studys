package util.Hive

import java.io.File

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Durations, StreamingContext}
import util.Redis.cluster.RedisUtils
import util.SparkTools.SparkUtils
import util.kafka.KafkaOffSetValidate

/**
  * Created by ZJ on 2019-7-5
  * comment:测试hive连接
  */
object HiveConnector {
  def main(args: Array[String]): Unit = {

    val warehouseLocation: String = new File("hdfs://10.149.1.150:8020/user/hive/warehouse").getAbsolutePath
    val sparkSession: SparkSession = SparkSession.builder()
      .config("spark.sql.warehouse.dir",warehouseLocation)
      .enableHiveSupport()
      .config(SparkUtils.getSparkConf)
      .getOrCreate()
    val ssc = new StreamingContext(sparkSession.sparkContext,  Durations.seconds(6))

    ssc.checkpoint("hdfs://10.149.1.150:8020/tmp/sparktest/checkpoint")
    var kafkaParams: Map[String, Object] = SparkUtils.getKafkaParams()
    kafkaParams += ("group.id" -> "groupc")
    val topics: Array[String] = Array("testopic1", "testopic2")

    val kafkaOffSetValidate = new KafkaOffSetValidate()
    val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams, kafkaOffSetValidate.getValidPartitionAndOffSet(kafkaParams, topics, "topic")))


    //SparkUtils.getSparkStreamingContext(10.toLong).sparkContext.getConf


    //BasicCall.businessProcess(sparkSession)
    sparkSession.sql("show databases").show
    println(sparkSession.sqlContext.sparkContext.getConf.getExecutorEnv)
    println("-----------------------------------------")
    //ServiceNetShow.businessProcess(sparkSession)
    stream.transform(rdd => {
      println(rdd.repartition(3).mapPartitionsWithIndex((a: Int, b: Iterator[ConsumerRecord[String, String]]) => {
        println("partitionindex===================>" + a.toString)
        println(a)
        b
      }, false).count())

      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      for (x <- offsetRanges) {
        RedisUtils.saveOffset2Redis(offsetRanges)
        println(x.topic + "|" + x.topicPartition() + "|" + x.untilOffset)
      }
      // 异步向kafka提交offset
      //stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      rdd
    }).map(msg => msg.value()).foreachRDD(rdd => {
      rdd.foreachPartition(i => i.foreach(a => println(a)))
    })


    ssc.start()
    ssc.awaitTermination()

    ssc.stop(false, true)

  }

}
