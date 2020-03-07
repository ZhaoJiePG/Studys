package WordCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ScalaWordCount {
  def main(args: Array[String]): Unit = {

    //创建spark的配置(设置本地setMaster("local[*]"))
    val conf = new SparkConf().setAppName("").setMaster("local[*]")

    //创建spark的执行入口
    val sc = new SparkContext(conf)

    //指定以后从哪里读取RDD
    val lines: RDD[String] = sc.textFile(args(0), 1)

    //wordcount
    val rdd: RDD[(String, Int)] = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
      .sortBy(_._2, false)

    rdd.collect().foreach(println)


//    rdd.saveAsTextFile(args(1))

    //释放资源
    sc.stop()


  }
}
