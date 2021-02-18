package SparkCore.Methods

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


/**
  * Created by ZJ on 2021/2/5
  * comment:broadcast广播变量
  * 分布式共享的只读变量
  * 广播变量可将任务中的闭包数据保存刀executor的内存中
  */
object BroadCastDemo extends App {
  val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[2]")
  val sc = new SparkContext(conf)

  val rdd1 = sc.makeRDD(List(("a",1),("b",2),("c",3)))
//  val rdd2 = sc.makeRDD(List(("a",4),("b",5),("c",6)))
  val map = mutable.Map(("a",4),("b",5),("c",6))
  //join会导致数据量几何增长，影响shuffle性能，不推荐试用，用广播变量替代
//  private val value: RDD[(String, (Int, Int))] = rdd1.join(r dd2)

  private var bc: Broadcast[mutable.Map[String, Int]] = sc.broadcast(map)

  rdd1.map{
    case (w,c) =>{
      //访问广播变量
      val l: Int = bc.value.getOrElse(w,0)
      (w,(c,l))
    }
  }.collect().foreach(println)

  sc.stop()
}
