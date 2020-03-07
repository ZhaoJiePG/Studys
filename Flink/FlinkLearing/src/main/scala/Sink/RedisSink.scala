package FlinkLearing.Sink

import FlinkLearing.Sink.KafkaSink.SensorReading
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
 * Created by ZJ on 2020-2-17
 * comment:
 */
object RedisSink {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //transform操作
    val inputStream: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")
    val dataStream = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      //将对象转换位string
      SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).toDouble)
    })

    val conf: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder()
        .setHost("localhost")
        .setPort(6379)
        .build()

    dataStream.addSink(new RedisSink(conf,new MyRedisMapper))
  }
}

class MyRedisMapper() extends RedisMapper[SensorReading]{
  //定义保存数据到redis的命令
  override def getCommandDescription: RedisCommandDescription = {
    //把id和温度保存成hash表 Hset key field value
    new RedisCommandDescription(RedisCommand.HSET,"sensor_tempeture")
  }

  //定义保存到redis的value
  override def getKeyFromData(data: SensorReading): String = data.temperature.toString

  //定义保存到redis的key
  override def getValueFromData(data: SensorReading): String = data.id
}

