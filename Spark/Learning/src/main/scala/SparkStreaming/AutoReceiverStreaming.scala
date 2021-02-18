package SparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable
import scala.util.Random

/**
  * Created by ZJ on 2021/2/6
  * comment:自定义receiver
  */
object AutoReceiverStreaming {
  def main(args: Array[String]): Unit = {

    // TODO 创建环境对象
    // StreamingContext创建时，需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期（采集周期）
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val inputStream: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver())

    inputStream.print()

    ssc.start()


    ssc.awaitTermination()
  }

  /**
    * 自定义数据采集器
    */
  class MyReceiver extends Receiver[String](StorageLevel.MEMORY_ONLY){
    private var flag = true

    override def onStart(): Unit = {
      new Thread(new Runnable {
        override def run(): Unit = {
          while (flag){
            val message = "采集的数据为" + new Random().nextInt(10).toString
            //存储
            store(message)
            Thread.sleep(500)
          }
        }
      })
    }

    override def onStop(): Unit = {
      flag = false;
    }
  }
}
