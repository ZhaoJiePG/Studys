package SparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by ZJ on 2021/2/6
  * comment:
  */
object WindowStreming1 extends App {
  // TODO 创建环境对象
  // StreamingContext创建时，需要传递两个参数
  // 第一个参数表示环境配置
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
  // 第二个参数表示批量处理的周期（采集周期）
  val ssc = new StreamingContext(sparkConf, Seconds(3))

  val lines: ReceiverInputDStream[String] = ssc.socketTextStream("10.149.8.23", 6666)

  private val ds: DStream[(String, Int)] = lines.map((_,1))

  //当窗口范围比较大，但是滑动幅度比较小，那么可以采用增加数据和删除数据的方式
  // 无需重复计算，提升性能。
  //第一个dunc 累加 第二个 func 去除之前分区里的数据
  private val windowDs: DStream[(String, Int)] = ds.reduceByKeyAndWindow(_+_,_-_,Seconds(9),Seconds(3))

  windowDs.print()
  // 1. 启动采集器
  ssc.start()
  // 2. 等待采集器的关闭
  ssc.awaitTermination()

}
