package RDD

import org.apache.spark.{SparkConf, SparkContext}

object RDD_Actions extends App{
  //构建spark配置
  val sparkconf = new SparkConf()
    .setMaster("local[2]").setAppName("Rdd_Transforms")

  //构建sparkcontext
  val sc = new SparkContext(sparkconf)

//    1、def reduce(f: (T, T) => T): T  如果最后不是返回RDD，那么就是行动操作，
  val list = 1 to 10
  val rdd1 = sc.parallelize(list,1)
  //print(rdd1.reduce(_+_))

//    2、 collect()  将RDD的数据返回到driver层进行输出,运行在Driver上
  //print(rdd1.collect)

//    3、def count(): Long  计算RDD的数据数量，并输出
  //print(rdd1.count())

//    4、first()  返回RDD的第一个元素
  //print(rdd1.first())

//    5、take(n) 返回RDD的前n个元素
  //println(rdd1.take(1))

//    6、takeSample  采样
  //print(rdd1.sample(withReplacement = false,0.2))

//    7、takeOrdered(n)  返回排序后的前几个数据

//    8、aggregate (zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U) 聚合操作,默认所有key相同

//    9、fold(zeroValue)(func)  aggregate的简化操作，seqOp和combOp相同，默认所有key相同

//    10、saveAsTextFile（path）  path为HDFS相兼容的路径

//    11、saveAsSequenceFile（path） 将文件存储为SequenceFile

//    12、saveAsObjectFile（path） 将文件存储为ObjectFile

//    13、def countByKey(): Map[K, Long]   返回每个Key的数据的数量。

//    14、foreach 对每一个元素进行处理。

}
