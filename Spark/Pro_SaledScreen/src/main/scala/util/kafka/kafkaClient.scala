package util.kafka

import java.util
import java.util.{Properties, UUID}

import com.alibaba.fastjson.JSONObject
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.util.Random

object kafkaClient {

  def main(args: Array[String]): Unit = {
    producer()
    //consumer()
  }

  def producer(): Unit = {
    println("开始产生消息！")

    val props = new Properties()
    //props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.149.1.202:9092,10.149.1.203:9092")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "ydhadoop01:9092,ydhadoop02:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.ACKS_CONFIG, "1")

    val producer = new KafkaProducer[String, String](props)
    while (true){
      val record = new JSONObject()
      record.put("vin",UUID.randomUUID())
      record.put("lon",Random.nextInt(5)+"")
      record.put("lat",Random.nextInt(5)+"")
      record.put("status","状态:"+Random.nextInt(1))
      record.put("totalMileage",Random.nextInt(4))
      record.put("resMileage",Random.nextInt(4))
      record.put("soc",Random.nextInt(3))
      record.put("soh",Random.nextInt(3))
      record.put("travel_speed",Random.nextInt(2))
      record.put("altitude","altitude:"+Random.nextInt(3))
      record.put("circleTimes",Random.nextInt(3))
      record.put("createTime",System.currentTimeMillis())
      record.put("type","gps")
      println(record.toJSONString)
      producer.send(new ProducerRecord("yadea_gps_shanghai", Random.nextInt(50)+"", record.toString))
      println("开始产生消息！")
      Thread.sleep(5000)
    }
    producer.close()
  }

  def consumer(): Unit = {

    val groupID = "client_consumer1"
    val topic = ""

    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ydhadoop01:9092,ydhadoop02:9092,ydhadoop03:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID)
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "client_consumer1")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")

    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(java.util.Arrays.asList("serviceDataTestTopic"))

    while (true) {
      val records: ConsumerRecords[String, String] = consumer.poll(200)
      val value: util.Iterator[ConsumerRecord[String, String]] = records.records("serviceDataTestTopic").iterator()
      while (value.hasNext){
        println(value.next().key()+ "|" + value.next().value())
      }
    }
    consumer.close()
  }
}
