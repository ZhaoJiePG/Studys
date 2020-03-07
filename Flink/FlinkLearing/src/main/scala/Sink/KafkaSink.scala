package FlinkLearing.Sink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011

/**
 * Created by ZJ on 2019/12/23
 * comment:
 */
object KafkaSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //transform操作
    val inputStream: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")
    val outStream: DataStream[String] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      //将对象转换位string
      val sensorReading: SensorReading = SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toLong)
      sensorReading.toString
    })

    //kafkaSink
    outStream.addSink(new FlinkKafkaProducer011[String]("kafka02:9092,kafka03:9092,kafka04:9092","test02",new SimpleStringSchema()))

    outStream.print("kafkaSink:")
    env.execute("kafkaStreaming")

  }
  case class SensorReading( id: String, timestamp: Long, temperature: Double )
}


