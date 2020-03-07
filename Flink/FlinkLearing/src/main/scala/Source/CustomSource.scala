package FlinkLearing.Source
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._

import scala.util.Random

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 自定义source
 */
object CustomSource {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val stream: DataStream[SensorReading] = env.addSource(new SensorSource())
    stream.print("CustomSource:")

  }
}

case class SensorReading( id: String, timestamp: Long, temperature: Double )

class SensorSource extends SourceFunction[SensorReading]{
  //定义一个flag，标识数据源是否正常运行
  var running = true

  //停止标识
  override def cancel(): Unit = {
    running = false
  }

  //正常运行标识
  override def run(sourceContext: SourceFunction.SourceContext[SensorReading]): Unit = {
    //初始化一个随机发生器
    val rand = new Random()

    //初始化定义一组传感器温度数据
    var curTemp: Seq[(String, Double)] = 1.to(10).map(
      i => ("sensor_"+i,60+rand.nextGaussian())
    )

    //用无限循环产生数据流
    while (running){
      //在前一次温度的基础上更新温度值
      curTemp = curTemp.map(
        t=>(t._1,t._2+rand.nextGaussian())
      )
      //获取当前时间戳
      val curTime = System.currentTimeMillis()
      curTemp.foreach(
        //使用ctx的collect方法发送数据
        t => sourceContext.collect(SensorReading(t._1,curTime,t._2))
      )
      Thread.sleep(500)
    }
  }
}