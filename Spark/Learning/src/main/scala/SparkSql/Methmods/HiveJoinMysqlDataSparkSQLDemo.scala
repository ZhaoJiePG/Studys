package Methmods

import java.util.Properties

import org.apache.spark.sql.{SaveMode, SparkSession}

object HiveJoinMysqlDataSparkSQLDemo {
  def main(args: Array[String]): Unit = {

    //1.6.1
   /* val conf = new SparkConf()
      .setMaster("local")
      .setAppName("HiveJoinMysqlDataSparkSQLDemo")

    val sc = SparkContext(conf)

    val sqlContext: HiveContext = new HiveContext(sc)*/
    //2.2.1
    val spark = SparkSession
      .builder()
      //.master("local")
      .appName("HiveJoinMysqlDataSparkSQLDemo")
      .enableHiveSupport()
      .getOrCreate()

    val sqlContext = spark.sqlContext

    val url = "jdbc:mysql://spark.yangpu.com:3306/yangpu0404"
    val table = "dept"
    val properties = new Properties()
    val username = "root"
    val password = "123456"
    properties.put("user",username)
    properties.put("password",password)
    properties.setProperty("batch.size","1024")

    //2.1hive中表的数据导入mysql中
    spark
      .read
      .table("ibeifeng.dept")
      .write
      .mode(SaveMode.Overwrite)
      .jdbc(url,table,properties)

    //2.2hive和mysql的表的join
    //1.读取mysql中的表形成临时表
    val df = spark
      .read
      .jdbc(url,table,Array("deptno<20","deptno >= 20 and deptno < 30","deptno >= 30"),properties)
    //df.registerTempTable("temp_dept")
    df.createTempView("tmp_dept")
    //如果你想删除临时表sqlContext.dropTempTable("tmp_dept")
    print(df.schema)

    //2.表的join
    spark
      .sql(
        """
          |select aa.empno,aa.ename,aa.dname,aa.loc from
          |(select a.*,b.dname,b.loc
          |from ibeifeng.emp a
          |join tmp_dept b on a.deptno = b.deptno) as aa  where aa.empno < '7900'
        """.stripMargin)
      .createTempView("tmp_emp_join_dept_result")

    /*1.6
    sqlContext
      .sql(
        """
          |select aa.empno,aa.ename,aa,dname,aa.loc from
          |(select a.*,b.dname,b.loc
          |from ibeifeng.emp a
          |join tmp_dept b on a.deptno = b.deptno) as aa  where aa.empno < '7900'
        """.stripMargin)
      .createTempView("tmp_emp_join_dept_result")*/
    //如果一张表被我们反复的利用，那么最好缓存下来
    //1.方式1：
      sqlContext.cacheTable("tmp_emp_join_dept_result")
    //2.方式2：
      //spark.read.table("tmp_emp_join_dept_result").cache()

    //3.数据输出
    //3.1输出到HDFS,默认是parquet文件
    /*sqlContext
      .read
      .table("tmp_emp_join_dept_result")
      .write
      .mode(SaveMode.Overwrite)
      .save("hdfs://spak.yangpu.com:8020/output/0404/sparksql/tmp_emp_join_dept_result")
*/
    spark
      .read
      .table("tmp_emp_join_dept_result")
      .write
      .mode(SaveMode.Overwrite)
      .save("hdfs://spark.yangpu.com:8020/output/0404/sparksql/tmp_emp_join_dept_result")

    //3.2
    spark
      .read
      .table("tmp_emp_join_dept_result")
      .write
      .format("parquet")
      .partitionBy("empno")
      .mode(SaveMode.Overwrite)
      .saveAsTable("tmp_emp_join_dept_result")



    spark.sql("select * from tmp_emp_join_dept_result").show()

    sqlContext.uncacheTable("tmp_emp_join_dept_result")







  }

}
