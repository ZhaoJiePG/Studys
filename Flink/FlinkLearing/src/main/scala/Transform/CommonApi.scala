package FlinkLearing.Transform

import org.apache.flink.streaming.api.scala._

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 常用转换api
 */
object CommonApi {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //读取数据
    val stream1: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")

    // Transform操作
    val stream2: DataStream[SensorReading] = stream1.map(data => {
      val dataList: Array[String] = data.split(",")
      val sensorReading: SensorReading = SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toLong)
      sensorReading
    })
    // 过滤操作
    val stream3: DataStream[SensorReading] = stream2.filter(_.temperature>10)

    // 1.聚合操作
    val stream4: DataStream[SensorReading] = stream2.keyBy(_.id).sum(1)
//    stream4.print().setParallelism(3)
    //返回的流中包含每一次聚合的结果，而不是
    //只返回最后一次聚合的最终结果
    val stream5: DataStream[SensorReading] = stream2.keyBy("id").reduce((x,y)=>SensorReading(x.id,x.timestamp+10,y.temperature+1))
    stream5.print().setParallelism(1)

    env.execute("transform test job")
  }
}
case class SensorReading( id: String, timestamp: Long, temperature: Double )

