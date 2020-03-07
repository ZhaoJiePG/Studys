package Methmods

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WindowDemo {
  /**
    * 有状态装换reducebykeyandwindow
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //创建配置
    val conf: SparkConf = new SparkConf().setAppName("HelloStream").setMaster("local[2]")

    //创建sparkcontext
    val ssc = new StreamingContext(conf,Seconds(2))

    //设置checkpointdir
    ssc.checkpoint("D:\\Maven\\Spark\\SparkStreaming\\checkpointdir")

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop01",9999)

    val result: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map((_,1))
    //.reduceByKey(_+_)

    val state: DStream[(String, Int)] = result.reduceByKeyAndWindow((a:Int,b:Int)=> a+b,Seconds(15),Seconds(5))

    state.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
