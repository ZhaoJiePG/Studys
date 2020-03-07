package FlinkLearing.Transform

import java.security.interfaces.DSAPublicKey

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.io.CheckpointableInputFormat
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * Created by ZJ on 2020-1-26
 * comment:
 * 底层api，访问当前时间戳，watermark，注册定时事件
 */
object ProcessFunApi {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //设置状态后端和checcheckpoint,默认情况下不开启
//    env.setStateBackend(new MemoryStateBackend())//内存
//    env.setStateBackend(new FsStateBackend(""))//磁盘

    //设置checkpointing
    env.enableCheckpointing(60000,CheckpointingMode.EXACTLY_ONCE)
//    env.getCheckpointConfig.setCheckpointInterval(6000)
//    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
//    env.getCheckpointConfig.setCheckpointTimeout(6000)
    //设置重启策略,重启次数,间隔时间
//    env.setRestartStrategy(RestartStrategies.failureRateRestart(3,Time.seconds(300),Time.seconds(500)))


    // 读取数据源
    val inputStream: DataStream[String] = env.socketTextStream("kafka05",8888)
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataList: Array[String] = data.split(',')
      new SensorReading(dataList(0).trim(),dataList(1).trim.toLong,dataList(2).trim.toDouble)
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(1)) {
      override def extractTimestamp(t: SensorReading): Long = t.timestamp*1000
    })

    //2秒内温度持续上升的
    val processStream1: DataStream[String] = dataStream.keyBy(_.id)
      .process(new TempIncreAlert())

    //2秒内温度超过10度报警
    //第一种方式：状态编程
//    val processStream2: DataStream[(String, Double, Double)] = dataStream.keyBy(_.id)
//      .process(new TempChangeAlert(10.0))

    //第二种方式flatmap
    val processStream2: DataStream[(String, Double, Double)] = dataStream.keyBy(_.id)
        .flatMap(new TempChangeAlert2(10.0))

    //第二种方式flatmapwithstatue
    val processStream3: DataStream[(String, Double, Double)] = dataStream.keyBy(_.id)
      //flatMapWithState输入：类型，状态 输出：类型，状态
      .flatMapWithState[(String,Double,Double),Double]{
        //如果没有状态，就是没有数据来过，把当前数据温度值存入状态
        case (input:SensorReading,None) => (List.empty,Some(input.temperature))
        //如果有状态，就应该与上次的状态里温度值比较差值，如果大于就输出报警
        case (input: SensorReading,lastTemp:Some[Double])=>{
          val diff: Double = (input.temperature - lastTemp.get).abs
          if(diff > 10.0){
            (List(),Some(input.temperature))
          }else{
            (List.empty,Some(input.temperature))
          }
        }
      }

    processStream1.print()
    env.execute()
  }
}

//自定义底层api
class TempIncreAlert() extends KeyedProcessFunction[String,SensorReading,String]{
  //定义状态，用来保存上一个数据的温度值
  lazy val lastTemp:ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp",classOf[Double]))
  //定义状态，用来保存定时器的时间戳
  lazy val currentTimer: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("currentTimer",classOf[Long]))

  override def processElement(value: SensorReading, context: KeyedProcessFunction[String, SensorReading, String]#Context, collector: Collector[String]): Unit = {
    //获取上一个温度值
    val preTemp: Double = lastTemp.value()
    //更新温度值
    lastTemp.update(value.temperature)
    //获取上一个定时器的时间戳
    val curTimerTs: Long = currentTimer.value()

    //温度上升且没有设置过定时器，则注册定时器
    if (value.temperature>preTemp && currentTimer == 0){
      //获取当前时间戳
      val timerTs: Long = context.timerService().currentProcessingTime()+1000L
      //注册定时器
      context.timerService().registerProcessingTimeTimer(timerTs)
      //更新定时器的时间戳
      currentTimer.update(timerTs)
    }//温度下降或者第一条数据来时
    else if(preTemp > value.temperature || preTemp == 0){
      //删除定时器的时间戳并清空
      context.timerService().deleteProcessingTimeTimer(curTimerTs)
      currentTimer.clear()
    }
  }

  //回调函数，逻辑
  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, SensorReading, String]#OnTimerContext, out: Collector[String]): Unit = {
    //输出报警信息
    out.collect(ctx.getCurrentKey + "温度持续上升")
    //清空状态
    currentTimer.clear()
  }
}

//第一种状态编程
class TempChangeAlert(threshold:Double) extends KeyedProcessFunction[String,SensorReading,(String,Double,Double)]{
  //定义一个状态变量，获取上一次温度
  lazy val lastTempState: ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("latTemp",classOf[Double]))

  override def processElement(value: SensorReading, context: KeyedProcessFunction[String, SensorReading, (String, Double, Double)]#Context, collector: Collector[(String, Double, Double)]): Unit = {
    //获取上次的温度值
    val lastTemp: Double = lastTempState.value()
    //用当前温度值和上次的温度值求差
    val diff: Double = (value.temperature-lastTemp).abs

    if(diff>threshold){
      collector.collect((value.id,lastTemp,value.temperature))
    }
    //更新状态
    lastTempState.update(value.temperature)
  }
}

//第二种flatmap(需要状态)继承富函数
class TempChangeAlert2(threshold:Double) extends RichFlatMapFunction[SensorReading,(String,Double,Double)]{
  private  var lastTempState:ValueState[Double] = _

  //open方法可以获取上下文变量，获取状态值
  override def open(parameters: Configuration): Unit = {
    //初始化的时候，声明statute变量
    lastTempState= getRuntimeContext.getState(new ValueStateDescriptor[Double]("latTemp",classOf[Double]))
  }

  override def flatMap(value: SensorReading, collector: Collector[(String, Double, Double)]): Unit = {
    //获取上次的温度值
    val lastTemp: Double = lastTempState.value()
    //用当前温度值和上次的温度值求差
    val diff: Double = (value.temperature-lastTemp).abs

    if(diff>threshold){
      collector.collect((value.id,lastTemp,value.temperature))
    }
    //更新状态
    lastTempState.update(value.temperature)
  }
}

//第三种flatmapwithstatue(需要状态)继承富函数
class TempChangeAlert3(threshold:Double) extends RichFlatMapFunction[SensorReading,(String,Double,Double)]{
  private  var lastTempState:ValueState[Double] = _

  //open方法可以获取上下文变量，获取状态值
  override def open(parameters: Configuration): Unit = {
    //初始化的时候，声明statute变量
    lastTempState= getRuntimeContext.getState(new ValueStateDescriptor[Double]("latTemp",classOf[Double]))
  }

  override def flatMap(value: SensorReading, collector: Collector[(String, Double, Double)]): Unit = {
    //获取上次的温度值
    val lastTemp: Double = lastTempState.value()
    //用当前温度值和上次的温度值求差
    val diff: Double = (value.temperature-lastTemp).abs

    if(diff>threshold){
      collector.collect((value.id,lastTemp,value.temperature))
    }
    //更新状态
    lastTempState.update(value.temperature)
  }
}