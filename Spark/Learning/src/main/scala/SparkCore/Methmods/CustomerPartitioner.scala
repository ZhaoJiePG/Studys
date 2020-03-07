package Methmods

import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
  * Created by ZJ on 2019-3-13
  * comment:
  */
object CustomerPartitioner {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Partitoner")
    val sc = new SparkContext(sparkConf)
    val rdd: RDD[(Int, Long)] = sc.makeRDD(0 to 10).zipWithIndex()

    val rdd1: Array[String] = rdd.mapPartitionsWithIndex((index, items)=>Iterator(index +":["+items.mkString(",")+"]")).collect()
    val rdd2: RDD[(Int, Long)] = rdd.partitionBy(new CustomerPartitioner(5))

    val rdd3: Array[String] = rdd2.mapPartitionsWithIndex((index, items)=>Iterator(index +":["+items.mkString(",")+"]")).collect()

    for (i<-rdd1){
      println(i)
    }
    for (i<-rdd2){
      println(i)
    }

    sc.stop()
  }
}
class CustomerPartitioner(numPartition:Int) extends Partitioner{
  //返回分区的总数
  override def numPartitions: Int = {
    numPartition
  }

  //返回分区的索引
  override def getPartition(key: Any): Int = {
    key.toString.toInt % numPartition
  }
}
