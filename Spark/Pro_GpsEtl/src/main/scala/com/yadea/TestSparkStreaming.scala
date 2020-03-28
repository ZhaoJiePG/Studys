package com.yadea

import java.io.File

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.commons.logging.{Log, LogFactory}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{ArrayType, DoubleType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies, OffsetRange}
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
 * Created by ZJ on 2020/3/27
 * comment:
 */
object TestSparkStreaming {
  def main(args: Array[String]): Unit = {
    val groupid ="test108"

    val logger: Logger = org.apache.log4j.Logger.getLogger(this.getClass)

    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.ERROR)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.ERROR)

    //hive元数据文件路径
    val warehouseLocation: String = new File("hdfs://10.149.1.203:8020/user/hive/warehouse").getAbsolutePath

    //配置日志格式和类型
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.ERROR)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.ERROR)
    org.apache.log4j.LogManager.resetConfiguration()
//    org.apache.log4j.PropertyConfigurator.configure("D:\\Maven\\Studys\\Spark\\Pro_GpsEtl\\src\\main\\resources\\log4j.properties")

    //新建sparkconf和配置参数
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local[4]").setAppName("TestSparkStreaming")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "20")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    sparkConf.set("spark.streaming.blockInterval", "5000ms")
    sparkConf.set("spark.streaming.receiver.maxRate", "20")
    sparkConf.set("spark.shuffle.io.maxRetries", "60")
    sparkConf.set("spark.shuffle.io.retryWait", "5s")
    sparkConf.set("spark.executor.heartbeatInterval", "60s")
    sparkConf.set("spark.default.parallelism", "8")
    sparkConf.set("spark.streaming.backpressure.enabled", "true")
    sparkConf.set("spark.streaming.backpressure.initialRate", "5000")
    sparkConf.set("spark.streaming.kafka.consumer.cache.enabled", "false")
    sparkConf.set("spark.driver.cores", "2")
    sparkConf.set("spark.reducer.maxSizeInFlight", "64M")
    sparkConf.set("spark.shuffle.file.buffer", "512K")

    //创建sparkSession客户端
    val spark: SparkSession = SparkSession.builder().config(sparkConf)
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    //创建sparkstreaming客户端
    val ssc = new StreamingContext(spark.sparkContext, Durations.seconds(10))
    //设置checkpoint路径
    ssc.checkpoint("hdfs://10.149.1.203:8020/tmp/TestSparkStreaming/checkpoint")
    ssc.sparkContext.setLogLevel("WARN")

    //指定kafka topics
    val topics: Array[String] = Array("test02")

    //配置kafka属性
    val kafkaParams: Map[String, Object] = Map[String, Object](
      "auto.offset.reset" -> "earliest"
      , "value.deserializer" -> classOf[StringDeserializer]
      , "key.deserializer" -> classOf[StringDeserializer]
      , "bootstrap.servers" -> "kafka02:9092,kafka03:9092,kafka04:9092"
      , "group.id" -> groupid
      //, "group.id" -> "test1"
      , "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    //获取kafka数据流
    val kafkaJsonData: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))


    print("start")

//    逻辑
    var kafkaJsonDatas: DStream[JSONObject] = kafkaJsonData.transform(_.map(item=>{
      JSON.parseObject(item.value())
    }))


    kafkaJsonDatas.foreachRDD(rdd=>{

      // 获取当前偏移量和value
      val rowRdd: RDD[Row] = rdd.map(item =>{
        val altitude: String = if (item.containsKey("altitude")) item.getString("altitude") else ""
        val id: String = if (item.containsKey("id")) item.getString("id") else ""
        val vin: String = if (item.containsKey("vin")) item.getString("vin") else ""
        val travel_speed: String = if (item.containsKey("travel_speed")) item.getString("travel_speed") else ""

        Row(id,altitude,vin,travel_speed)
      })

      //创建gps schema
      val schema: StructType = StructType(Seq(StructField("vin", StringType, nullable = true)
        , StructField("altitude", StringType, nullable = true)
        , StructField("id", StringType, nullable = true)
        , StructField("travel_speed", StringType, nullable = true)
      ))

      val testDF: DataFrame = spark.createDataFrame(rowRdd,schema)
      testDF.createOrReplaceTempView("testsparkstreaming")

      spark.sql("select * from testsparkstreaming").show()

      rdd.foreach( item =>{
        println("id为"+item.getString("id")+"当前数据为"+item)
      })
    })

    //提交kafka偏移量
    kafkaJsonData.foreachRDD { rdd =>
      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // some time later, after outputs have completed
      kafkaJsonData.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)

    }

    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = false, stopGracefully = true)

  }
}
