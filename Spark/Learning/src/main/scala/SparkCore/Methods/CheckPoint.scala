package SparkCore.Methods

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by ZJ on 2021/2/5
  * comment:checkpoint操作
  */
object CheckPoint {
  val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[2]")
  val sc :SparkContext = new SparkContext(conf)
  sc.setCheckpointDir("./checkpoint/1.txt")


  val rdd1: RDD[String] = sc.parallelize(Array("ab", "bc"))

  //checkpoint需要落磁盘
  rdd1.checkpoint()
}
