package Window

import FlinkLearing.Transform.SensorReading
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.functions.{AssignerWithPeriodicWatermarks, AssignerWithPunctuatedWatermarks}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * Created by ZJ on 2020-1-22
 * comment:
 *
 * event time:事件创建时间
 * lngestion time:数据进入Flink的时间
 * processing time:执行操作算子的本地时间，与机器无关
 *
 *
 */
object Demo {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //周期性生产watermark的参数设置
//    env.getConfig.setAutoWatermarkInterval(100L)

    // 读取数据源
    val inputStream: DataStream[String] = env.socketTextStream("kafka05",8888)
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      new SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toDouble)
    })
    //指定时间戳和timemark
//        .assignAscendingTimestamps(_.timestamp*1000)//有序数据
//        .assignTimestampsAndWatermarks(new MaAssigner1())
        .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(1)) {
          override def extractTimestamp(t: SensorReading): Long = t.timestamp*1000
        })
dataStream.print("dataStream")

    //统计十秒内的最小温度
    val minTempPerWindowStream1: DataStream[(String, Double)] = dataStream
      .map(data => (data.id, data.temperature))
      .keyBy(_._1)
      //  keyBy之后指定时间戳和timestamp触发每个key
      //      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(1)) {
      //        override def extractTimestamp(t: SensorReading): Long = t.timestamp
      //      })
      .timeWindow(Time.seconds(10)) //开窗时间窗口
      .reduce((data1, data2) => (data1._1, data2._2.min(data1._2)))//用reduce做增量聚合
    minTempPerWindowStream1.print("minTempPerWindowStream")

    //统计十秒内的最小温度,间隔5秒输出一次，水位线左闭右开
    val minTempPerWindowStream2: DataStream[(String, Double)] = dataStream
      .map(data => (data.id, data.temperature))
      .keyBy(_._1)
//    .window(SlidingEventTimeWindows.of(Time.seconds(10),Time.seconds(10),Time.hours(-8)))
      .timeWindow(Time.seconds(10),Time.seconds(5)) //开窗时间窗口
      .reduce((data1, data2) => (data1._1, data2._2.min(data1._2)))//用reduce做增量聚合
    minTempPerWindowStream1.print("minTempPerWindowStream")

    /**
     * timemark初始值,windowsize是滑动步长
     * return timestamp - (timestamp - offset + windowSize) % windowSize
     * offset时区，中国是-8
     * timestamp - timestamp % windowsize
     *
     * if (offset >= 0L && offset < slide && size > 0L) {
     *     this.size = size;
     *     this.slide = slide;
     *     this.offset = offset;
     * }
     */

    env.execute()
  }
  case class SensorReading( id: String, timestamp: Long, temperature: Double )
}



//自定义时间水印(周期性生成)
class MaAssigner1() extends AssignerWithPeriodicWatermarks[SensorReading]{
  val bound = 2000
  var maxTs: Long = Long.MinValue

  //获取当前watermark
  override def getCurrentWatermark: Watermark = new Watermark(maxTs-bound)

  //抽取时间戳
  override def extractTimestamp(element: SensorReading, time: Long): Long = {
    maxTs = maxTs.max(element.timestamp)
    element.timestamp
  }
}

//自定义时间水印(非周期性生成)
class MaAssigner2() extends AssignerWithPunctuatedWatermarks[SensorReading]{
  override def checkAndGetNextWatermark(t: SensorReading, l: Long): Watermark = new Watermark(l)

  override def extractTimestamp(t: SensorReading, l: Long): Long = t.timestamp
}