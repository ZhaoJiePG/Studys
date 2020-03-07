package Methmods

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by ZJ on 2019-3-13
  * comment:
  */
object OverFunctions {

  case class Score(name:String,cass:Int,score:Int)
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf()
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()

    import spark.implicits._
    println("============原始数据===========")
    val scoreDF: DataFrame = spark.sparkContext.makeRDD(Array(Score("a",1,80),
      Score("b",1,78),Score("c",1,95),Score("d",2,74),Score("e",2,92),
      Score("f",3,99),Score("g",3,99),Score("h",3,58),Score("i",3,78)))
      .toDF("naem","class","score")
    scoreDF.createOrReplaceTempView("score")

    //求每个班最高成绩的学生信息
    spark.sql(
      """
        |select A.* from score A,
        |(select class,max(score) as max from score group by class))B
        |where A.score = B.max and a.class = b.class
      """.stripMargin)
    //求每个班最高成绩的学生信息
    spark.sql(
      """
        |select name,class,score,
        |rank() over(partiton by class order by score desc) rank
        |from score as A
        |where A.rank = 1
      """.stripMargin)
    spark.sql(
      """
        |
      """.stripMargin)

  }
}
