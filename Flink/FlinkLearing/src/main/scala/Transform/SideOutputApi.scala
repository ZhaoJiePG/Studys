package FlinkLearing.Transform

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * Created by ZJ on 2020-1-27
 * comment:
 * SideOutputApi 侧输出流 select和select
 * 冰点报警，如果小于32F，输出报警信息到侧输出流
 */
object SideOutputApi {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 读取数据源
    val inputStream: DataStream[String] = env.socketTextStream("kafka05",8888)
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      new SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toDouble)
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(1)) {
      override def extractTimestamp(t: SensorReading): Long = t.timestamp * 1000
    })

    //2秒内温度持续上升的
    val processStream: DataStream[String] = dataStream.keyBy(_.id)
      .process(new FreezingAlert())

    //打印主流
    processStream.print("process data")
    //打印侧输出流
    processStream.getSideOutput(new OutputTag[String]("frezing alert"))
    env.execute()
  }
}

//自定义侧输出
class FreezingAlert() extends ProcessFunction[SensorReading,String]{
  override def processElement(value: SensorReading, context: ProcessFunction[SensorReading, String]#Context, out: Collector[String]): Unit = {
    lazy val alertOutput = new OutputTag[String]("frezing alert")

    if(value.temperature <32.0){
      //侧输出流
      context.output(alertOutput,"freezing alert for"+value.id)
    }//输出到主流
    else{
      out.collect(value.toString)
    }
  }
}