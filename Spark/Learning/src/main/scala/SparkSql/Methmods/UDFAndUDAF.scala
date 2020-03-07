package Methmods

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql._

/**
  * Created by ZJ on 2019-3-13
  * comment:
  */
object UDFAndUDAF {
  def main(args: Array[String]): Unit = {

    val sparkconf = new SparkConf().setMaster("local[2]").setAppName("HelloWorld")

    val spark = SparkSession.builder()
      .config(sparkconf)
      .getOrCreate()

    val student:DataFrame = spark.read.json("D:\\IdeaData\\Spark\\Spark\\SparkSql\\Data\\student.json")

    student.createOrReplaceTempView("student")

    //1.UDF函数和注册
    spark.udf.register("add",(x:String)=> "A" + x)
    spark.sql("select add(name) from student")

    //2.UDAF函数，多对多，常用和group by一起用
    //自定义UDAF函数，目标：求平均工资，[工资总额，工资个数]
    //A:弱类型
    spark.udf.register("average",new AverageSal)
    spark.sql("select average(salary) from employee").show()

    //B:强类型
    //val averagesal = new AverageSal().toColumn.name("averagee")
    student.select("averagee").show()
  }
}
//弱类型
class AverageSal extends UserDefinedAggregateFunction{
  //输入类型
  override def inputSchema: StructType = StructType(StructField("salary",LongType)::Nil)
  //每一个分区中的共享累加变量
  override def bufferSchema: StructType = StructType(StructField("sum",LongType)::StructField("count",IntegerType)::Nil)
  //UDAF函数输出类型
  override def dataType: DataType = DoubleType
  //输入和输出判断是否相同
  override def deterministic: Boolean = true
  //初始化每一个分区中的共享变量
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0
  }
  //每一个分区需要聚合的时候调用该方法
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    //获取这一行的工资，将工资加入到sum中
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    //将工资的个数+1
    buffer(1) = buffer.getInt(1) + 1
  }
  //将没一个分区里的数据合并
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    //合并总的工资
    buffer1(0) = buffer1.getLong(0)+buffer2.getLong(0)
    //合并总的工资个数
    buffer1(1) = buffer1.getInt(1) + buffer2.getInt(1)
  }
  //计算
  override def evaluate(buffer: Row): Any = {
    //取出工资、总的工资个数
    buffer.getLong(0).toDouble/buffer.getInt(1)
  }
}

//强类型
case class Employee(name:String,salary:Long)
case class Aver(var sum: Long,var count: Int)
class AverageStrong extends Aggregator[Employee,Aver,Double] {
  //初始化 初始化每一个分区中的共享变量
  override def zero: Aver = Aver(0L, 0)

  //每一个分区中的每一条数据聚合时需要调用该方法
  override def reduce(b: Aver, a: Employee): Aver = {
    b.sum = b.sum + a.salary
    b.count = b.count + 1
    b
  }

  //将每一个分区的输出，合并，形成最后的数据
  override def merge(b1: Aver, b2: Aver): Aver = {
    b1.sum = b1.sum + b2.sum
    b1.count = b1.count + 1
    b1
  }

  //给出计算结果
  override def finish(reduction: Aver): Double = {
    reduction.sum.toDouble / reduction.count
  }

  //主要用于对共享变量进行编码
  override def bufferEncoder: Encoder[Aver] = Encoders.product

  //主要用于将输出进行编码
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
