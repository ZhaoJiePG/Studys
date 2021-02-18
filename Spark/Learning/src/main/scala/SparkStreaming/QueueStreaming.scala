package SparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
  * Created by ZJ on 2021/2/6
  * comment:
  */
object QueueStreaming {
  def main(args: Array[String]): Unit = {

    // TODO 创建环境对象
    // StreamingContext创建时，需要传递两个参数
    // 第一个参数表示环境配置
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    // 第二个参数表示批量处理的周期（采集周期）
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val rddQueue = new mutable.Queue[RDD[Int]]()

    val inputStream: InputDStream[Int] = ssc.queueStream(rddQueue,oneAtATime = false)

    val res: DStream[(Int, Int)] = inputStream.map((_,1)).reduceByKey(_+_)

    res.print()

    ssc.start()

    for (i <- 1 to 5){
      rddQueue += ssc.sparkContext.makeRDD(1 to 300,10)
      Thread.sleep(1000)
    }

    ssc.awaitTermination()
  }

}
