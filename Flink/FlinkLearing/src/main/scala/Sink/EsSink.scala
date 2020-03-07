package FlinkLearing.Sink

import java.util

import FlinkLearing.Sink.KafkaSink.SensorReading
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests
import org.elasticsearch.index.seqno.GlobalCheckpointSyncAction.Request



/**
 * Created by ZJ on 2020-2-17
 * comment:
 */
object EsSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //transform操作
    val inputStream: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      //将对象转换位string
      SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).toDouble)
    })

    val httpHost = new util.ArrayList[HttpHost]()
    httpHost.add(new HttpHost("localhost",9200))

    //创建一个esSink的builder
    val esSinkBuilder = new ElasticsearchSink.Builder[SensorReading](
      httpHost,
      new ElasticsearchSinkFunction[SensorReading] {
        override def process(element: SensorReading, ctx: RuntimeContext, indexer: RequestIndexer): Unit = {
          print("saving data:"+element)
          //包装成Hashmap后者json
          val json = new util.HashMap[String,String]()
          json.put("sensor_id",element.id)
          json.put("temperature_id",element.temperature.toString)
          json.put("timestamp_id",element.timestamp.toString)

          //创建index request 准备发送数据
          val indexRequest: IndexRequest = Requests.indexRequest()
            .index("sensor")
            .`type`("readingData")
            .source(json)

          //利用index发送请求，写入数据
          indexer.add(indexRequest)
        }
      })
    dataStream.addSink(esSinkBuilder.build())
  }
}
