package DEMOS;

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext};
/**
  * Created by ZJ on 2019-3-6
  * comment:
  */
object GroupFavTeacher2 {
  def main(args: Array[String]): Unit = {
    val subjects = Array("bigdata","javaee","php")

    val conf = new SparkConf().setAppName("FavTeacher").setMaster("local[4]")
    val sc = new SparkContext(conf)

    //指定以后从哪里读取数据
    val lines: RDD[String] = sc.textFile("/input/teacher.log")

    //整理数据
    val SubjAndTeatcher: RDD[((String, String), Int)] = lines.map(line=>{
      val index: Int = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      val subject = line.substring(0,index).split("[.]")(0).split("/")(2)
      ((subject,teacher),1)
    })

    //聚合:将学科和老师联合当key
    val reducer: RDD[((String, String), Int)] = SubjAndTeatcher.reduceByKey(_+_)

    //scala的集合排序是在内存中进行的，但是内存可能用完，调用RDD的sortby内存加磁盘
    //此PDD仅仅有一个学科数据
    for (sub <- subjects){

      val filtered: RDD[((String, String), Int)] = reducer.filter(_._1._1 == sub)

      //调用RDD的sortby方法(take是个action，会触发任务提交)
      val r: Array[((String, String), Int)] = filtered.sortBy(_._2,false).take(3)

      println(r.toBuffer)
    }
    sc.stop()
  }

}
