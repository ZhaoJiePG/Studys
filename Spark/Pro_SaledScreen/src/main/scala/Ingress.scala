import jdbc.JDBCHelperUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import util.Redis.cluster.RedisUtils
import util.SparkTools.SparkUtils
import util.kafka.KafkaOffSetValidate

/**
  * Created by ZJ on 2019-7-5
  * comment:
  */

object Ingress {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.ERROR)
    Logger.getLogger("org.apache.kafka.clients.consumer").setLevel(Level.ERROR)

    val sparkSession: SparkSession = SparkUtils.getSparkSession
    sparkSession.sparkContext.setLogLevel(logLevel = "WARN")
    val ssc: StreamingContext = SparkUtils.getSparkStreamingContext(20)
    sparkSession.sql("show databases").show()
    var kafkaParams: Map[String, Object] = SparkUtils.getKafkaParams()
    kafkaParams += ("group.id" -> "groupc")

    val topics: Array[String] = Array("testopic1", "testopic2")
    val kafkaOffSetValidate = new KafkaOffSetValidate()

    //创建stream
    val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams, kafkaOffSetValidate.getValidPartitionAndOffSet(kafkaParams, topics, "topic")))

    //开始,执行业务逻辑
    stream.transform(rdd =>{
      //打印分区信息
      println(rdd.repartition(3).mapPartitionsWithIndex((a,b)=>{
        println("partiitin index===========》"+a.toString)
        b
      }))

      //新建mysql连接
      val jdbc: JDBCHelperUtils = new JDBCHelperUtils

      //维修员分布
      servicenet.RepairPersonShow.businessProcess(sparkSession,jdbc)

      //获取kafka信息，redis保存偏移量
      val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      for (x<- offsetRanges){
        RedisUtils.saveOffset2Redis(offsetRanges)
        println("sava 2 redis"+x.topic+"|"+x.topicPartition()+"|"+x.untilOffset)
      }
      // 异步向kafka提交offset
      //stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
      rdd
    }).map(msg => msg.value()).foreachRDD(rdd =>{
      rdd.foreachPartition(i => i.foreach(a=>println(a)))
    })

    ssc.start()
    ssc.awaitTermination()

    ssc.stop(stopSparkContext = false, stopGracefully = true)
  }
}
