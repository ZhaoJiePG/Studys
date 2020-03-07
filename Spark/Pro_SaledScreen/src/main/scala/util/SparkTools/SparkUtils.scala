package util.SparkTools

import java.io.File
import java.lang

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
  * Created by ZJ on 2019-7-5
  * comment:
  */
class SparkUtils {
  private var sparkConf: SparkConf = _

  private var sparkSession: SparkSession = _

  private var sparkStreamingContext: StreamingContext = _

  def getSparkConf: SparkConf = {
    if (sparkConf == null) {
      val warehouseLocation: String = new File("hdfs://10.149.1.150:8020/user/hive/warehouse").getAbsolutePath
      sparkConf = new SparkConf()
        .setMaster("local[*]")
        .setAppName("saledRealTimeStreamingAndSQL")
      sparkConf.set("spark.streaming.backpressure.enabled", "true")
      sparkConf.set("spark.streaming.backpressure.initialRate", "1")
      sparkConf.set("spark.streaming.blockInterval", "1000ms")
      sparkConf.set("spark.streaming.receiver.maxRate", "1")
      sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "1")
      sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true")
      sparkConf.set("spark.default.parallelism", "3")


      //      sparkConf.set("spark.shuffle.io.maxRetries", "10")
      //      sparkConf.set("spark.shuffle.io.retryWait", "10")
      sparkConf.set("spark.shuffle.io.maxRetries", "60")
      sparkConf.set("spark.shuffle.io.retryWait", "5s")
      sparkConf.set("spark.executor.heartbeatInterval", "60s")
      sparkConf.set("spark.default.parallelism", "6")
      sparkConf.set("spark.reducer.maxSizeInFlight", "128M")
      sparkConf.set("spark.shuffle.file.buffer", "512K")
      sparkConf.set("spark.driver.cores", "2")
      sparkConf.set("spark.executor.cores","3")
      sparkConf.set("spark.executor.memory", "6G")
      sparkConf.set("spark.driver.memory", "2G")


      sparkConf.set("spark.streaming.kafka.consumer.cache.enabled", "false")
      sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      sparkConf.set("spark.sql.warehouse.dir", warehouseLocation)
      sparkConf.set("", "")
      //      sparkConf.set("spark.streaming.concurrentJobs", "10")
      //      sparkConf.set("spark.streaming.kafka.maxRetries", "50")
    }
    sparkConf
  }

  def getSparkSession(sparkConf: SparkConf): SparkSession = {
    if (sparkSession == null) {
      sparkSession = SparkSession.builder().enableHiveSupport().config(sparkConf).getOrCreate()
    }
    sparkSession
  }

  def getSparkStreamingContext(second: Int): StreamingContext = {
    if (sparkStreamingContext == null) {
      sparkStreamingContext = new StreamingContext(getSparkSession(getSparkConf).sparkContext, Durations.seconds(second))
      sparkStreamingContext.checkpoint("hdfs://10.149.1.150:8020/tmp/sparktest/checkpoint")
    }
    sparkStreamingContext
  }
}

object SparkUtils {

  private val sparkUtilsClass = new SparkUtils

  def getSparkConf: SparkConf = {
    sparkUtilsClass.getSparkConf
  }

  def getSparkSession: SparkSession = {
    sparkUtilsClass.getSparkSession(getSparkConf)
  }

  def getSparkStreamingContext(second: Int): StreamingContext = {
    sparkUtilsClass.getSparkStreamingContext(second)
  }

  def getKafkaParams(input: List[(String, Object)] = null): Map[String, Object] = {
    val stringToObject: Map[String, Object] = Map[String, Object]("bootstrap.servers" -> "ydhadoop03:9092,ydhadoop01:9092,ydhadoop02:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: lang.Boolean),
      "heartbeat.interval.ms" -> "3000",
      "session.timeout.ms" -> "60000")
    if (input != null && input.nonEmpty) {
      stringToObject ++ input
    } else {
      stringToObject
    }
  }

  def getSparkSQLForJDBConfigOptions(myDefind: List[(String, String)] = null): Map[String, String] = {
    var options: Map[String, String] = Map("url" -> "jdbc:sqlserver://10.149.1.26:1433;DatabaseName=BasicCallDB",
      "username" -> "ydsa", "password" -> "simperfect123.", "driver" -> "com.microsoft.sqlserver.jdbc.SQLServerDriver")
    if (null != myDefind && myDefind.nonEmpty) {
      for ((k, v) <- myDefind) {
        options += (k -> v)
      }
      options
    } else {
      options
    }
  }

}