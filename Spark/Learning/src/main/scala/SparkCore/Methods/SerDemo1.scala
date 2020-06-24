package SparkCore.Methods

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SerDemo1 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SerDemo").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rdd: RDD[String] = sc.parallelize(Array("hello world", "hello atguigu", "atguigu", "hahah"), 2)

    val searcher = new Searcher("hello")
    val result: RDD[String] = searcher.getMatchedRDD1(rdd)

    result.collect.foreach(println)
  }
}

class Searcher1(val query: String) extends Serializable {
  // 判断s中是否包括字符串query
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }

  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD1(rdd: RDD[String]): RDD[String] ={
    rdd.filter(isMatch)
  }

  // 过滤出包含 query字符串的字符串组成的新的 RDD
  def getMatchedRDD2(rdd: RDD[String]): RDD[String] ={
    rdd.filter(_.contains(query))
  }
}
