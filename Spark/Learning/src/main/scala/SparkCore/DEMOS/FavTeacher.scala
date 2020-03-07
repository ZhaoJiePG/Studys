package DEMOS

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.{BufferedSource, Source};

/**
  * Created by zx on 2017/10/8.
  */
object FavTeacher {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("FavTeacher").setMaster("local[4]")
    val sc = new SparkContext(conf)

    //指定以后从哪里读取数据
    val source: BufferedSource = Source.fromFile("D:\\Maven\\JAVA\\JAVA\\Spark\\src\\main\\Datas\\teacher.log")
    val list: List[String] = source.getLines().toList
    val lines: RDD[String] = sc.parallelize(list)
    //整理数据
    val teacherAndOne = lines.map(line=>{
      val index: Int = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      (teacher,1)
    })

    //聚合
    val reduced: RDD[(String, Int)] = teacherAndOne.reduceByKey(_+_)
    //排序
    val sorted: RDD[(String, Int)] = reduced.sortBy(_._2, false)
    //触发Action执行计算
    val reslut: Array[(String, Int)] = sorted.collect()

    //打印
    println(reslut.toBuffer)

    sc.stop()
  }
}
