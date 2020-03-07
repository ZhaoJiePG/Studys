package Methmods

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * Created by ZJ on 2019-3-13
  * comment:
  */
object RDD2DFrame2Dataset extends App {

  val sparkconf = new SparkConf().setMaster("local[2]").setAppName("HelloWorld")

  val spark = SparkSession.builder()
    .config(sparkconf)
    .getOrCreate()

  val sc = spark.sparkContext

  val student = sc.textFile("D:\\IdeaData\\Spark\\Spark\\SparkSql\\Data\\student.txt")

  val rdd1 :RDD[(String, String)] = student.map(x => {
    val para: Array[String] = x.split(",")
    (para(0),para(1))
  })

  //1.Rdd转换成Dframe
  //第一种方式，利用隐式转换，手动确定schema
  import spark.implicits._
  val df1 : DataFrame = rdd1.toDF("name","age")
  df1.show()
  //第二种方式，通过反射
  val df3: DataFrame = rdd1.toDF()
  //第三种方式：通过编程方式
  import org.apache.spark.sql.types._
  val schemaString = StructType(StructField("name",StringType)::StructField("age",StringType)::Nil)
  val fields = student.map(x =>{
    val para: Array[String] = x.split(",")
    Row(para(0),para(1).trim.toInt)}
  )
  val df4 = spark.createDataFrame(fields,schemaString)



  //2.Dframe转换成RDD
  val rdd2 :RDD[Row] = df1.rdd

  //RDD转换成DataSet,使用caseclass缺点schema
  case class Student(name:String,age:Int)

  val ds1 :Dataset[Student]= student.map(x => {
    val para: Array[String] = x.split(",")
    Student(para(0), para(1).toInt)
  }).toDS()

  val rdd3: RDD[Student] = ds1.rdd

  //3.DataSet转换成DataFrame,直接转换
  val df2: DataFrame = ds1.toDF()

  //4.DataFrame转换成DataSet(Schema需要借助case class)[DF的列名要和case class的一致]
  val ds2: Dataset[Student] = df2.as[Student]
}
