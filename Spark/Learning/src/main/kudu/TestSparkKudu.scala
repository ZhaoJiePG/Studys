import java.util

import org.apache.kudu.client.CreateTableOptions
import org.apache.kudu.spark.kudu._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by ZJ on 2021/2/13
  * comment:
  */
object TestSparkKudu extends App {
  //构建sparkConf
  private val conf: SparkConf = new SparkConf().setAppName("TestSparkKudu").setMaster("local[*]")

  private val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

  private val sc: SparkContext = sparkSession.sparkContext

  sc.setLogLevel("warn")

  //构建kuducontext对象 用于kudujava客户端交互 操作kudu数据
  val kuduMaster = "hadoopprd03:7051,hadoopprd04:7051,hadoopprd05:7051"
  private val kuduContext = new KuduContext(kuduMaster,sc)

  val tableName = "Student"

  val kuduOption = Map(
    "kudu.master" -> kuduMaster,
    "kudu.table" -> tableName
  )

  //创建表
//  createTable(kuduContext)

  //插入数据
//  insertTable(kuduContext)

  //更新数据
//  upsetData(kuduContext)

  //删除数据
//  deleteData(kuduContext)

  //查询数据
//  queryData(kuduContext)

  //使用dateFrame写数据
  val date = List(Student(5,"test5",20,2))
  val studentRDD: RDD[Student] = sc.parallelize(date)
  import sparkSession.implicits._
  val dataFrame: DataFrame = studentRDD.toDF()
  //dataFrame只支持追加，不支持覆盖
  dataFrame.write.mode("append").options(kuduOption).kudu

  //使用dataframe读取数据
  private val kudu: DataFrame = sparkSession.read.options(kuduOption).kudu
  kudu.show()
  kudu.rdd.foreach(println)


  sparkSession.close()

  /**
    * 删除数据
    * @param kuduContext
    */
  def deleteData(kuduContext: KuduContext) = {
    //数据准备
    val date = List(Student(1,"test1",20,1))
    val studentRDD: RDD[Student] = sc.parallelize(date)
    import sparkSession.implicits._
    val dataFrame: DataFrame = studentRDD.toDF().select("id")
    kuduContext.deleteRows(dataFrame,tableName)
  }

  /**
    * 更新并插入数据
    * @param kuduContext
    * @return
    */
  def upsetData(kuduContext: KuduContext) = {
    //数据准备
    val date = List(Student(1,"test1",20,1),Student(2,"test4",24,2),Student(3,"test3",20,2))
    val studentRDD: RDD[Student] = sc.parallelize(date)
    import sparkSession.implicits._
    val dataFrame: DataFrame = studentRDD.toDF()

    kuduContext.upsertRows(dataFrame,tableName)
  }

  /**
    * 查询数据
    * @param kuduContext
    * @return
    */
  def queryData(kuduContext: KuduContext) = {
    //指定查询表的字段
    val columnProjection = List("id","name","age","sex")
    val value: RDD[Row] = kuduContext.kuduRDD(sc,tableName,columnProjection)
    value.foreach(println)
  }

  /**
    * 插入数据操作
    */
  def insertTable(kuduContext: KuduContext)={
    //准备待插入的数据
    val date = List(Student(1,"test1",20,1),Student(2,"test2",21,1),Student(3,"test3",20,2))
    val studentRDD: RDD[Student] = sc.parallelize(date)
    import sparkSession.implicits._
    val dataFrame: DataFrame = studentRDD.toDF()

    kuduContext.insertRows(dataFrame,tableName)
  }

  /**
    * 创建表
    */
  def createTable(kuduContext: KuduContext): Unit ={
    //通过kuducontext创建一张表(参数 表名 schema 主键 option)
    val schema = StructType(
      //名称，类型，是否为空
      StructField("id",IntegerType,false) ::
        StructField("name",StringType,false) ::
        StructField("age",IntegerType,false) ::
        StructField("sex",IntegerType,false) ::
        Nil
    )

    val keys: Seq[String] = Seq("id")
    val arrayList = new util.ArrayList[String]()
    arrayList.add("id")

    val options = new CreateTableOptions()
    options.addHashPartitions(arrayList,3)

    //该表时候存在
    if(kuduContext.tableExists(tableName)){
      kuduContext.deleteTable(tableName)
    }else{
      kuduContext.createTable(tableName,schema,keys,options)
    }
  }

}

//样例类，用于封装数据
case class Student(id:Int,name:String,age:Int,sex:Int)
