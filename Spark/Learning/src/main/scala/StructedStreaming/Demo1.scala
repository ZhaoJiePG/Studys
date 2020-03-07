import org.apache.spark.sql.{DataFrame, SparkSession}
import org.joda.time.DateTime

/**
  * Created by ZJ on 2019-6-25
  * comment:
  */

object Demo1 {
  val spark = SparkSession.builder().appName("StructedStreamingDemo")
    .getOrCreate()

  val lines: DataFrame = spark.readStream
    .format("socket").option("host", "127.0.0.1")
    .option("port", 9999).load()

  private val df: DataFrame = lines.toDF()

  df.select("device").where("singel > 20")

  df.groupBy("type").count()                          // using untyped API

}
case class DeviceData(device: String, typee: String, signal: Double, time: DateTime)

