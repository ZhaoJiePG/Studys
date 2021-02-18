package SparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
  * Created by ZJ on 2021/2/6
  * comment:
  */
object StateStreaming {
  def main(args: Array[String]): Unit = {
    // TODO 创建环境对象
    // StreamingContext创建时，需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期（采集周期）
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    ssc.checkpoint("D:\\Maven\\Studys\\Spark\\Learning\\src\\main\\scala\\SparkStreaming\\checkpoint.txt")

    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._

    // TODO 逻辑处理
    // 获取端口数据
    val nc1: ReceiverInputDStream[String] = ssc.socketTextStream("10.149.8.23", 6666)

    val words = nc1.flatMap(_.split(" "))

    val wordToOne = words.map((_,1))

//    val wordToCount: DStream[(String, Int)] = wordToOne.reduceByKey(_+_)

    // 根据key对数据进行状态更新
    // 第一个值表示相同key的value数据
    // 第二个值表示缓存区相同key的value数据
    val wordToCount = wordToOne.updateStateByKey(
      (seq:Seq[Int] , buffer:Option[Int])=>{
      val newCount = buffer.getOrElse(0) + seq.sum
      Option(newCount)
    })

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
