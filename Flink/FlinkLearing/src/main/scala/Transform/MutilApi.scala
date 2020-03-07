package FlinkLearing.Transform

import org.apache.flink.streaming.api.scala._

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 多流转换算子
 */
object MutilApi {
  case class SensorReading( id: String, timestamp: Long, temperature: Double )
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //读取数据
    val stream1: DataStream[String] = env.readTextFile("D:\\Maven\\SparkFlink\\Flink\\src\\main\\resources\\sensor.txt")

    val stream2: DataStream[SensorReading] = stream1.map(data => {
      val dataList: Array[String] = data.split(",")
      val sensorReading: SensorReading = SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toLong)
      sensorReading
    })

    //拆分
    val splitStream: SplitStream[SensorReading] = stream2.split(data => {
      if (data.temperature > 4) Seq("high") else Seq("low")
    })

    val high: DataStream[SensorReading] = splitStream.select("high")
    val low: DataStream[SensorReading] = splitStream.select("low")
    val all: DataStream[SensorReading] = splitStream.select("low","high")

//    high.print("high:")
//    low.print("low:")
//    all.print("all:")

    //合并
    //connect只能两条流合成
    val warning: DataStream[(String, Double)] = high.map(x =>(x.id,x.temperature))
    val connectStream: ConnectedStreams[(String, Double), SensorReading] = warning.connect(low)
    val coMapStream: DataStream[Product] = connectStream.map(
      warningData => (warningData._1,warningData._2,"warning"),
      lowData => (lowData.id,"healthy")
    )

//    coMapStream.print().setParallelism(3)

    //union可以合并多条流，数据结构必须一样Ds->Ds
    val unionStream: DataStream[SensorReading] = low.union(high)
    unionStream.print()

    env.execute("transform test job")
  }
}
