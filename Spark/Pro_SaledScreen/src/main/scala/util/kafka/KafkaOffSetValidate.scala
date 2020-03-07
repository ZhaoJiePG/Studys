package util.kafka

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import util.Redis.cluster.RedisUtils
import scala.collection.JavaConversions._
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object KafkaOffSetValidate {
  def main(args: Array[String]): Unit = {
    val kafkaParams: Map[String, Object] = Map[String, Object]("bootstrap.servers" -> "ydhadoop01:9092,ydhadoop02:9092"
      ,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer]
      , "group.id" -> "groupc"
      //      "auto.offset.reset" -> "earliest",
      //      "enable.auto.commit" -> (false: java.lang.Boolean
      //        )

    )
    val t = new KafkaOffSetValidate()
    //    val offset: Map[TopicPartition, Long] = t.getCurrentOffset(kafkaParams, List("testopic1","testopic2"))
    //    for ((a, b) <- offset) {
    //      println(a.topic() + "|" + a.partition() + "|" + b)
    //    }
    // t.getEarliestAndLatestOffsets(kafkaParams, List("testopic1", "testopic2"))
    print(t.getValidPartitionAndOffSet(kafkaParams, List("testopic1", "testopic2"), "topic"))
  }
}

class KafkaOffSetValidate {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  /**
    * batch offset check
    *
    * @param kafkaParams              config consumer
    * @param topics                   topic
    * @param redisKeyForStorageOffSet redis key
    * @return checkedOffSet
    */
  def getValidPartitionAndOffSet(kafkaParams: Map[String, Object], topics: Iterable[String], redisKeyForStorageOffSet: String): Map[TopicPartition, Long] = {

    val checkedOffSet: mutable.Set[(String, Int, Long)] = mutable.Set[(String, Int, Long)]()
    val preStorageOffSet: mutable.Iterable[(String, Int, Long)] = RedisUtils.getOffsetFromRedis(redisKeyForStorageOffSet).map(result => (result._1.split("_")(0), result._1.split("_")(1).toInt, result._2.toLong))
    val metaClusterOffSet: List[(String, Int, String, Long)] = getEarliestAndLatestOffsets(kafkaParams, topics)
    if (preStorageOffSet.isEmpty) {
      metaClusterOffSet.filter(_._3 == "earliest").map(a => checkedOffSet += ((a._1, a._2, a._4)))
      RedisUtils.saveCheckedOffsetInfo2Redis(checkedOffSet.toSeq)
      return checkedOffSet.map(row => new TopicPartition(row._1, row._2) -> row._3).toMap
    } else checkedOffSet ++= preStorageOffSet.toSet

    // [storage offset & cluster] offset range check
    metaClusterOffSet.foreach(x => {
      preStorageOffSet.foreach(y => {
        if ("earliest" == x._3) {
          if (x._1 == y._1 && x._2 == y._2 && y._3 < x._4) checkedOffSet += ((x._1, x._2, x._4))
        } else if ("latest" == x._3) {
          if (x._1 == y._1 && x._2 == y._2 && y._3 > x._4) checkedOffSet += ((x._1, x._2, x._4))
        }
      })
    })

    // [storage offset & cluster] offset do difference and result store
    val fromCluster: Set[(String, Int)] = metaClusterOffSet.filter(_._3 == "earliest").map(a => (a._1, a._2)).toSet
    val fromDefineStorage: Set[(String, Int)] = preStorageOffSet.map(x => (x._1, x._2)).toSet
    val diffInfo: Set[(String, Int)] = fromCluster.diff(fromDefineStorage)
    if (diffInfo.nonEmpty) {
      diffInfo.foreach(k => {
        metaClusterOffSet.filter(_._3 == "earliest").foreach(g => {
          if (k._1 == g._1 && k._2 == g._2) checkedOffSet += ((k._1, g._2, g._4))
        })
      })
    }
    RedisUtils.saveCheckedOffsetInfo2Redis(checkedOffSet.toSeq)
    checkedOffSet.map(row => new TopicPartition(row._1, row._2) -> row._3).toMap
  }

  /**
    * get smaller and highest offset
    *
    * @param kafkaParams config consumer
    * @param topics      topic
    * @return
    */
  def getEarliestAndLatestOffsets(kafkaParams: Map[String, Object], topics: Iterable[String]): List[(String, Int, String, Long)] = {
    val listBuffer: ListBuffer[(String, Int, String, Long)] = new ListBuffer[(String, Int, String, Long)]
    getEarliestOffsets(kafkaParams, topics).foreach(t => listBuffer.add((t._1.topic(), t._1.partition(), "earliest", t._2)))
    getLatestOffsets(kafkaParams, topics).foreach(t => listBuffer.add((t._1.topic(), t._1.partition(), "latest", t._2)))
    listBuffer.toList
  }


  /**
    * get small offset
    *
    * @param consumer   kafka consumer
    * @param partitions partition
    * @return
    */
  def getEarliestOffsets(consumer: Consumer[_, _], partitions: Set[TopicPartition]): Map[TopicPartition, Long] = {
    consumer.seekToBeginning(partitions)
    partitions.map(tp => tp -> consumer.position(tp)).toMap
  }

  /**
    * get the earliest (lowest) available offsets
    *
    * @param kafkaParams kafka client config
    * @param topics      subscribe topic
    */
  def getEarliestOffsets(kafkaParams: Map[String, Object], topics: Iterable[String]): Map[TopicPartition, Long] = {
    val newKafkaParams: mutable.Map[String, Object] = mutable.Map[String, Object]()
    newKafkaParams ++= kafkaParams
    newKafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    val consumer: KafkaConsumer[String, Array[Byte]] = new KafkaConsumer[String, Array[Byte]](newKafkaParams)
    consumer.subscribe(topics)
    consumer.poll(0)
    val parts: Set[TopicPartition] = consumer.assignment().toSet
    consumer.pause(parts)
    consumer.seekToBeginning(parts)
    val offsets: Map[TopicPartition, Long] = parts.map(tp => tp -> consumer.position(tp)).toMap
    for ((a, b) <- offsets) {
      println("the small offset ->topic:" + a.topic() + "|partition:" + a.partition() + "|offset:" + b)
    }
    consumer.unsubscribe()
    consumer.close()
    offsets
  }

  /**
    * get the latest (highest) available offset
    *
    * @param consumer   kafka consumer
    * @param partitions partition
    * @return
    */
  def getLatestOffsets(consumer: Consumer[_, _], partitions: Set[TopicPartition]): Map[TopicPartition, Long] = {
    consumer.seekToEnd(partitions)
    partitions.map(tp => tp -> consumer.position(tp)).toMap
  }

  /**
    * get the latest (highest) available offset
    *
    * @param kafkaParams kafka client config
    * @param topics      subscribe topic
    **/
  def getLatestOffsets(kafkaParams: Map[String, Object], topics: Iterable[String]): Map[TopicPartition, Long] = {
    val newKafkaParams: mutable.Map[String, Object] = mutable.Map[String, Object]()
    newKafkaParams ++= kafkaParams
    newKafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    val consumer: KafkaConsumer[String, Array[Byte]] = new KafkaConsumer[String, Array[Byte]](newKafkaParams)
    consumer.subscribe(topics)
    consumer.poll(0)
    val parts: Set[TopicPartition] = consumer.assignment().toSet
    consumer.pause(parts)
    consumer.seekToEnd(parts)
    val offsets: Map[TopicPartition, Long] = parts.map(tp => tp -> consumer.position(tp)).toMap
    for ((a, b) <- offsets) {
      println("the latest offset->topic:" + a.topic() + "|partition:" + a.partition() + "|offset:" + b)
    }
    consumer.unsubscribe()
    consumer.close()
    offsets
  }

  /**
    * get consumer offset
    *
    * @param consumer   kafka consumer
    * @param partitions partition
    * @return
    */
  def getCurrentOffsets(consumer: Consumer[_, _], partitions: Set[TopicPartition]): Map[TopicPartition, Long] = {
    partitions.map(tp => tp -> consumer.position(tp)).toMap
  }

  /**
    * get offsets
    *
    * @param kafkaParams kafka config
    * @param topics      topic
    * @return
    */
  def getCurrentOffset(kafkaParams: Map[String, Object], topics: Iterable[String]): Map[TopicPartition, Long] = {
    val offsetResetConfig: String = kafkaParams.getOrElse(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest").toString.toLowerCase
    val newKafkaParams: mutable.Map[String, Object] = mutable.Map[String, Object]()
    newKafkaParams ++= kafkaParams
    newKafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none")
    val consumer: KafkaConsumer[String, Array[Byte]] = new KafkaConsumer[String, Array[Byte]](newKafkaParams)
    consumer.subscribe(topics)
    val notOffsetTopicPartition: mutable.Set[TopicPartition] = mutable.Set[TopicPartition]()
    try {
      consumer.poll(50)
    } catch {
      case ex: NoOffsetForPartitionException =>
        logger.warn(s"consumer topic partition offset not found:${ex.partition()}")
        notOffsetTopicPartition.add(ex.partition())
    }
    // get all topic's partition by input topic
    val parts: Set[TopicPartition] = consumer.assignment().toSet
    consumer.pause(parts)


    // 获取有效的TopicPartition, 过滤掉没有offset的partition
    //    val validTopicPartition: Set[TopicPartition] = parts.diff(notOffsetTopicPartition)
    //    validTopicPartition.foreach(t => println(t.topic() + "^^^^"+ t.partition()))


    //    //获取指定消费组的当前offset
    val currentOffset: mutable.Map[TopicPartition, Long] = mutable.Map[TopicPartition, Long]()
    //    validTopicPartition.foreach(x => {
    //      try {
    //        currentOffset.put(x, consumer.position(x))
    //        println("获取指定消费组的当前offset" + " ########" + x.topic() + "#" + x.partition()+"#" +consumer.position(x))
    //      } catch {
    //        case ex: NoOffsetForPartitionException =>
    //          logger.warn(s"consumer topic partition offset not found:${ex.partition()}")
    //          notOffsetTopicPartition.add(ex.partition())
    //      }
    //    })

    //获取earliestOffset
    val earliestOffset: Map[TopicPartition, Long] = getEarliestOffsets(consumer, parts)
    for ((a, b) <- earliestOffset) {
      println("print debug earliestOffset info--->topic:" + a.topic() + "|partition:" + a.partition() + "|offset:" + b)
    }
    earliestOffset.foreach(x => {
      val value: Option[Long] = currentOffset.get(x._1)
      if (value.isEmpty) {
        currentOffset(x._1) = x._2
      } else if (value.get < x._2) {
        logger.warn(s"kafka data is lost from partition:${x._1} offset ${value.get} to ${x._2}")
        currentOffset(x._1) = x._2
      }
    })


    //获取lastOffset
    val lastOffset: Map[TopicPartition, Long] = getLatestOffsets(consumer, parts)
    for ((a, b) <- lastOffset) {
      println("print debug lastOffset info--->topic:" + a.topic() + "|partition:" + a.partition() + "|offset:" + b)
    }
    earliestOffset.foreach(x => {
      val value: Option[Long] = currentOffset.get(x._1)
      if (value.isEmpty) {
        currentOffset(x._1) = x._2
      } else if (value.get < x._2) {
        logger.warn(s"kafka data is lost from partition:${x._1} offset ${value.get} to ${x._2}")
        currentOffset(x._1) = x._2
      }
    })

    //获取lastOffset
    val latestOffset: Map[TopicPartition, Long] = if (offsetResetConfig.equalsIgnoreCase("earliest")) {
      getEarliestOffsets(consumer, parts)
    } else {
      getLatestOffsets(consumer, parts)
    }
    latestOffset.foreach(x => {
      val value: Option[Long] = currentOffset.get(x._1)
      if (value.isEmpty || value.get > x._2) {
        currentOffset(x._1) = x._2
      }
    })
    consumer.unsubscribe()
    consumer.close()
    currentOffset.toMap
  }
}
