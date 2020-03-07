package com.yadea

import java.io.File
import java.math.BigDecimal

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.yadea.util.{AddrConvert, BroadCastMapData, DateScalaUtils}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Durations, StreamingContext}

import scala.collection.mutable.ListBuffer

/**
  * Created by ZJ on 2019-6-29
  * comment:
  */
object DataProcess extends Logging {
  def main(args: Array[String]): Unit = {
    //hive元数据文件路径
    val warehouseLocation: String = new File("hdfs://10.149.1.150:8020/user/hive/warehouse").getAbsolutePath

    //配置日志格式和类型
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.ERROR)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.ERROR)
    org.apache.log4j.LogManager.resetConfiguration()
    org.apache.log4j.PropertyConfigurator.configure("/log4j.properties")

    //新建sparkconf和配置参数
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local[4]").setAppName("gps_etl_data")
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
    ssc.checkpoint("hdfs://10.149.1.150:8020/tmp/spark_gps/checkpoint")
    ssc.sparkContext.setLogLevel("WARN")

    //指定kafka topics
    val topics: Array[String] = Array("yeade_gps")

    //配置kafka属性
    val kafkaParams: Map[String, Object] = Map[String, Object](
      "auto.offset.reset" -> "earliest"
      , "value.deserializer" -> classOf[StringDeserializer]
      , "key.deserializer" -> classOf[StringDeserializer]
      , "bootstrap.servers" -> "ydhadoop03:9092,ydhadoop01:9092,ydhadoop02:9092"
      , "group.id" -> "line1"
      //, "group.id" -> "test1"
      , "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    //广播变量
    val faultMap: Broadcast[Map[String, (String, String)]] = ssc.sparkContext.broadcast(BroadCastMapData.getFaultsInfoMap)
    val alarmMap: Broadcast[Map[Int, String]] = ssc.sparkContext.broadcast(BroadCastMapData.getAlarmTypeMap)
    val warnMap: Broadcast[Map[Int, String]] = ssc.sparkContext.broadcast(BroadCastMapData.getWarningTypeMap)
    val oldFlaultsMap: Broadcast[Map[String, Map[Int, (String, String, String)]]] = ssc.sparkContext.broadcast(BroadCastMapData.getOldFaultMap)


    //获取kafka数据流
    val kafkaJsonData: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))

    //过滤kafka中数据为空的
    val kafkaValue: DStream[ConsumerRecord[String, String]] = kafkaJsonData.filter(_.value().isEmpty == false)

    //获取gps数据
    val kafkaValues: DStream[String] = kafkaValue.transform(rdd => {
      rdd.map(_.value())
    }).filter(it => {
      //获取type类型为gps的数据
      JSON.parseObject(it).getString("type") == "gps"
    })

    kafkaValues.foreachRDD(rdd => {
      //逻辑==============
      val positionInfo: RDD[(JSONObject, Seq[String], Seq[String], Seq[String], Seq[String], JSONObject)] = rdd.map(str => {
        //1.讲经纬度信息转换为JSON
        val temp: JSONObject = JSON.parseObject(str)
        //2.经纬度信息转换为百度api地址
        AddrConvert.fix2Address(gps2fix(temp.getString("lon")), gps2fix(temp.getString("lat")), temp)
      })
      //过滤不为空的RDD
      val filterRDD: RDD[(JSONObject, Seq[String], Seq[String], Seq[String], Seq[String], JSONObject)] = positionInfo.filter(_ != null)
      if (!filterRDD.isEmpty()){

        //创建row格式数据
        val rowRDD: RDD[Row] = filterRDD.map(row=>{
          //保存实际数据
          val temp: JSONObject = row._6
          val vin:String = if (temp.containsKey("vin")) temp.getString("vin") else ""
          val imei: String = if (temp.containsKey("imei")) temp.getString("imei") else ""
          val gps_lon: Long = if (temp.containsKey("lon")) temp.getLong("lon") else 0
          val gps_lat: Long = if (temp.containsKey("lat")) temp.getLong("lat") else 0
          val bike_status: String = if (temp.containsKey("status")) temp.getString("status") else ""
          val total_mileage: Int = if (temp.containsKey("totalMileage")) temp.getIntValue("totalMileage") else 0
          val res_mileage: Int = if (temp.containsKey("resMileage")) temp.getIntValue("resMileage") else 0
          val soc: Int = if (temp.containsKey("soc")) temp.getIntValue("soc") else 0
          val soh: Int = if (temp.containsKey("soh")) temp.getIntValue("soh") else 0
          val travel_speed: Int = if (temp.containsKey("travel_speed")) temp.getIntValue("travel_speed") else 0
          val turn: Int = if (temp.containsKey("turn")) temp.getIntValue("turn") else 0
          val altitude: String = if (temp.containsKey("altitude")) temp.getString("altitude") else ""
          val circle_times: Int = if (temp.containsKey("circleTimes")) temp.getIntValue("circleTimes") else 0
          val create_time: Long = if (temp.containsKey("createTime")) temp.getLong("createTime") else 0

          //返回ROW格式数据
          Row(vin, imei
            , gps_lon
            , gps_lat
            , bike_status
            , total_mileage
            , res_mileage
            , soc
            , soh
            , travel_speed
            , turn
            , altitude
            , circle_times
            , create_time
            , gps2fix(temp.getString("lon")), gps2fix(temp.getString("lat"))
            , row._1.getOrDefault("formatted_address", "")
            , row._1.getOrDefault("sematic_description", "")
            , row._2
            , row._1.getOrDefault("country", "")
            , row._1.getOrDefault("province", "")
            , row._1.getOrDefault("city", "")
            , row._1.getOrDefault("district", "")
            , row._1.getOrDefault("street", "")
            , row._1.getOrDefault("town", "")
            , row._2
            , row._3
            , row._4
            , DateScalaUtils.getCurrStamp, DateScalaUtils.getYMDStr(temp.getString("createTime")))
        })
        //创建gps schema
        val gpsSchema: StructType = StructType(Seq(StructField("vin", StringType, nullable = true)
          , StructField("imei", StringType, nullable = true)
          , StructField("gps_lon", LongType, nullable = true)
          , StructField("gps_lat", LongType, nullable = true)
          , StructField("bike_status", StringType, nullable = true)
          , StructField("total_mileage", IntegerType, nullable = true)
          , StructField("res_mileage", IntegerType, nullable = true)
          , StructField("soc", IntegerType, nullable = true)
          , StructField("soh", IntegerType, nullable = true)
          , StructField("travel_speed", IntegerType, nullable = true)
          , StructField("turn", IntegerType, nullable = true)
          , StructField("altitude", StringType, nullable = true)
          , StructField("circle_times", IntegerType, nullable = true)
          , StructField("create_time", LongType, nullable = true)
          , StructField("lon", DoubleType, nullable = true)
          , StructField("lat", DoubleType, nullable = true)
          , StructField("formatted_address", StringType, nullable = true)
          , StructField("sematic_description", StringType, nullable = true)
          , StructField("business", ArrayType(StringType, containsNull = true), nullable = true)
          , StructField("country", StringType, nullable = true)
          , StructField("province", StringType, nullable = true)
          , StructField("city", StringType, nullable = true)
          , StructField("district", StringType, nullable = true)
          , StructField("street", StringType, nullable = true)
          , StructField("town", StringType, nullable = true)
          , StructField("pois_name", ArrayType(StringType, containsNull = true), nullable = true)
          , StructField("pois_type", ArrayType(StringType, containsNull = true), nullable = true)
          , StructField("pois_tag", ArrayType(StringType, containsNull = true), nullable = true)
          , StructField("etl_stamp", LongType, nullable = true)
          , StructField("event_day", StringType, nullable = true)
        ))

        //创建dataframe
        val gpsDF: DataFrame = spark.createDataFrame(rowRDD,gpsSchema)
        //插入hive动态分区表
        gpsDF.createOrReplaceTempView("tempGpsTable")
        spark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
        spark.sql("set hive.exec.dynamic.partition=true")
        val sq = "insert into table ods.s05_t_bike_shanghai_gps PARTITION (event_day) select * from tempGpsTable"
        spark.sql(sq)

      }

    })

    // 车辆故障新协议
    kafkaValue.transform(rdd => {
      rdd.map(r => r.value())
    }).filter(r => {
      val temp: JSONObject = JSON.parseObject(r)
      temp.getString("type") == "newFault"
    }).foreachRDD(rdd => {
      println("newFault rdd时候为空:" + rdd.isEmpty())
      if (!rdd.isEmpty()) {
        val rddData: RDD[ListBuffer[Row]] = rdd.map(row => {
          val temp: JSONObject = JSON.parseObject(row)
          println(temp)
          val rows: ListBuffer[Row] = new ListBuffer[Row]
          val faults: JSONArray = temp.getJSONArray("allFaults")
          if (faults.size() > 0) {
            var faultCode: String = null
            for (x <- 0 until faults.size()) {
              // 根据allFaults中报警code,提取对应的报警信息
              faultCode = faults.get(x).toString
              val singleFault: (String, String) = faultMap.value.getOrElse(faultCode, ("其他", "其他"))

              rows.+=(Row(temp.getString("vin"), faultCode
                , singleFault._1, singleFault._2
                , alarmMap.value.getOrElse(temp.getIntValue("alarmType"), "其他")
                , warnMap.value.getOrElse(temp.getIntValue("warning_type"), "其他")
                , temp.getLong("createTime")
                , DateScalaUtils.getYMDStr(temp.getString("createTime"))))
            }
          }
          rows
        })
        rddData.flatMap(_.iterator)
        println("新协议 个数:" + rddData.count())

        val bikeFaultNewSchema = StructType(Seq(StructField("vin", StringType, nullable = true)
          , StructField("fault_code", StringType, nullable = true)
          , StructField("fault_detail", StringType, nullable = true)
          , StructField("fault_comment", StringType, nullable = true)
          , StructField("alarm_type", StringType, nullable = true)
          , StructField("warning_type", StringType, nullable = true)
          , StructField("createTime", LongType, nullable = true)
          , StructField("event_day", StringType, nullable = true)
        ))

        val bikeFaultNewDF: DataFrame = spark.createDataFrame(rddData.flatMap(_.toList.iterator), bikeFaultNewSchema)
        println("--------------------分割线-----------新协议---------------")
        bikeFaultNewDF.show(10)
        bikeFaultNewDF.createOrReplaceTempView("tempFaultNewTable")
        spark.sql("set hive.exec.dynamic.partition.mode=nonstrict")
        spark.sql("set hive.exec.dynamic.partition=true")
        val sq = "insert into table ods.s05_t_bike_shanghai_fault_new PARTITION (event_day) select * from tempFaultNewTable"
        spark.sql(sq)
        println("新协议 插入hive成功了")
      }
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

  def gps2fix(fix: String): Double = {
    val temp: Double = fix.toDouble / (30000 * 60)
    val bigDecimal = new BigDecimal(temp)
    bigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue
  }
}
