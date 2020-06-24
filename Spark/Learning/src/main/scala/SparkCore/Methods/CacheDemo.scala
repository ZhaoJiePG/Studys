package SparkCore.Methods

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object CacheDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val rdd1: RDD[String] = sc.parallelize(Array("ab", "bc"))
    val rdd2: RDD[String] = rdd1.flatMap(x => {
      println("flatMap...")
      x.split("")
    })
    // rdd2.cache() // 等价于 rdd2.persist(StorageLevel.MEMORY_ONLY)
    rdd2.persist(StorageLevel.MEMORY_ONLY)
    val rdd3: RDD[(String, Int)] = rdd2.map(x => {
      (x, 1)
    })

    rdd3.collect.foreach(println)
    println("-----------")
    rdd3.collect.foreach(println)
  }
}
