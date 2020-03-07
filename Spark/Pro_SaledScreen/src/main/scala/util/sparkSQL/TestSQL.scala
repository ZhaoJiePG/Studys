package util.sparkSQL

import org.apache.spark.sql.{DataFrame, SparkSession}


object TestSQL {
  def main(args: Array[String]): Unit = {

    val t1: Long = System.currentTimeMillis()
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("Spark SQL basic example")

      .getOrCreate()
    val cologDataFrame: DataFrame = spark.read.format("jdbc")
      .option("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .option("url", "jdbc:sqlserver://10.149.1.26:1433;DatabaseName=BasicCallDB")
      .option("username", "ydsa")
      .option("password", "simperfect123.")
      .option("driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
      .option("dbtable", "dbo.COLOG").load()
    val inCallCount: Long = cologDataFrame.where("CallType = 0").dropDuplicates("COID").count()
    println("呼入总量----->" + inCallCount)
    // For implicit conversions like converting RDDs to DataFrames
    val t2: Long = System.currentTimeMillis()
    print(t2 - t1)
  }
}
