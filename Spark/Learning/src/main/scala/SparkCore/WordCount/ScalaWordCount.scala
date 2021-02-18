package WordCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ScalaWordCount {
  def main(args: Array[String]): Unit = {

    //创建spark的配置(设置本地setMaster("local[*]"))
    val conf: SparkConf = new SparkConf().setAppName("wordcount").setMaster("local[*]")

    //创建spark的执行入口
    val sc = new SparkContext(conf)

    //指定以后从哪里读取RDD
    val lines: RDD[String] = sc.textFile("D:\\Maven\\Studys\\Spark\\Learning\\src\\main\\scala\\Datas\\ip.txt", 1)

    //wordcount
    val rdd: RDD[(String, Int)] = lines.flatMap(_.split("|")).map((_, 1)).reduceByKey(_ + _)
      .sortBy(_._2, false)

    rdd.collect().foreach(println)


//    rdd.saveAsTextFile(args(1))

    //释放资源
    sc.stop()


  }
}
//Error:scalac: Output path D:\Maven\Studys\JavaEe\target\test-classes is shared between: Module 'DataBoot' tests, Module 'JpaSpring' tests, Module 'MybatisAnnontation' tests, Module 'MybatisDynamicSql' tests, Module 'MybatisPlusStudy' tests, Module 'MybatisiDAO' tests, Module 'PrestoBoot' tests, Module 'QuickSpringBoot' tests, Module 'SpringAnnotion' tests, Module 'SpringAopAnnotion' tests, Module 'SpringAopXml' tests, Module 'SpringBootWeb' tests, Module 'SpringIocXml' tests, Module 'SpringMVC' tests
//Output path D:\Maven\Studys\Flink\target\classes is shared between: Module 'Flink' production, Module 'FlinkLearing' production
//Output path D:\Maven\Studys\JavaEe\target\classes is shared between: Module 'DataBoot' production, Module 'ELK' production, Module 'JavaEe' production, Module 'JavaSpiders' production, Module 'JpaSpring' production, Module 'JpaStart' production, Module 'Mybatis' production, Module 'MybatisAnnontation' production, Module 'MybatisCRUD' production, Module 'MybatisDynamicSql' production, Module 'MybatisPlus' production, Module 'MybatisPlusStudy' production, Module 'MybatisiDAO' production, Module 'PrestoBoot' production, Module 'QuickSpringBoot' production, Module 'SpidersBasic' production, Module 'Spring' production, Module 'SpringAnnotion' production, Module 'SpringAopAnnotion' production, Module 'SpringAopXml' production, Module 'SpringBoot' production, Module 'SpringBootWeb' production, Module 'SpringBoots' production, Module 'SpringDataJpa' production, Module 'SpringIocXml' production, Module 'SpringMVC' production, Module 'SwaggerBoot' production, Module 'ThreadExecutor' production
//Please configure separate output paths to proceed with the compilation.
//TIP: you can use Project Artifacts to combine compiled classes if needed.