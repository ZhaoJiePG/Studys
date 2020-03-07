package Methmods

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by ZJ on 2019-3-7
  * comment:
  */
object CacheCheckpoint {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("FavTeacher").setMaster("local[4]")
    val sc = new SparkContext(conf)

    val wc: RDD[Int] = sc.parallelize(List(1,2,3,4))

    //把常用的数据放入缓存
    wc.persist()
    wc.cache()
    wc.unpersist(true)

    //checkpoint机制,后续计算会从ck中拿取
    sc.setCheckpointDir("hdfs://node:.....")
    wc.checkpoint()

    sc.stop()
  }
}
