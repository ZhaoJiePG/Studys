package com.yadea

import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession

/**
  * Created by ZJ on 2019/8/21
  * comment:获取hiveschema
  */
object HiveSchema extends Logging{

  def main(args: Array[String]): Unit = {

    val logger: Logger = org.apache.log4j.Logger.getLogger(this.getClass)

    //hive元数据文件路径
    val warehouseLocation: String = new File("hdfs://10.149.1.203:8020/user/hive/warehouse").getAbsolutePath

    //配置日志格式和类型
    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.ERROR)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.ERROR)

    //新建sparkconf和配置参数
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("local[4]").setAppName("HiveSchema")

    logger.warn("=====new sparkconf=====")


    //创建sparkSession客户端
    val spark: SparkSession = SparkSession.builder().config(sparkConf)
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()
    logger.warn("=====new parkSession=====")

    spark.sql("show databases").show()
    logger.warn("=====查看所有数据库名称=====")

    spark.stop()
  }
}
