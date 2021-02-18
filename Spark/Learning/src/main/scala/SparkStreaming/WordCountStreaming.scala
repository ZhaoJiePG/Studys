package SparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by ZJ on 2021/2/6
  * comment:
  */
object WordCountStreaming{

  def main(args: Array[String]): Unit = {

    // TODO 创建环境对象
    // StreamingContext创建时，需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期（采集周期）
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._

    // TODO 逻辑处理
    // 获取端口数据
    val nc1: ReceiverInputDStream[String] = ssc.socketTextStream("10.149.8.23", 4444)
    val nc2: ReceiverInputDStream[String] = ssc.socketTextStream("10.149.8.23", 6666)

    nc2.foreachRDD(item => {
      val df: DataFrame = spark.read
        .format("jdbc")
        .option("url", "jdbc:mysql://10.149.1.127:3306/test")
        .option("driver", "com.mysql.jdbc.Driver")
        .option("user", "root")
        .option("password", "root")
        .option("dbtable", "cc_user")
        .load()
      df.take(10).foreach(println)
    })

    val words: DStream[String] = nc1.flatMap(_.split(" "))

    val wordToOne: DStream[(String, Int)] = words.map((_,1))

    val wordToCount: DStream[(String, Int)] = wordToOne.reduceByKey(_+_)

    wordToCount.print()

    // 由于SparkStreaming采集器是长期执行的任务，所以不能直接关闭
    // 如果main方法执行完毕，应用程序也会自动结束。所以不能让main执行完毕
    //ssc.stop()
    // 1. 启动采集器
    ssc.start()
    // 2. 等待采集器的关闭
    ssc.awaitTermination()
  }

}
