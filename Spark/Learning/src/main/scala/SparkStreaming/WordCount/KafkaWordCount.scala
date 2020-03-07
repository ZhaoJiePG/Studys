package WordCount

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by ZJ on 2019-3-18
  * comment:
  */
object KafkaWordCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("kwc").setMaster("local[*]")

    val ssc = new StreamingContext(conf,Seconds(5))

    val zkQuorum = "node1:2181,node2:2181,node3:2181"
    val groupId = "g1"
    val topid: Map[String, Int] = Map[String,Int]("xiaoniu" -> 1)

    //创建kafaka的DStream
    val lines: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zkQuorum,groupId,topid)

    //对数据进行处理
    //Kafka的ReceiverInputStream里封装的是一个元祖(key是写入的key，value是实际的数据)
    val res: DStream[(String, Int)] = lines.map(_._2).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    res.print

    ssc.start()
    ssc.awaitTermination()

  }

}
