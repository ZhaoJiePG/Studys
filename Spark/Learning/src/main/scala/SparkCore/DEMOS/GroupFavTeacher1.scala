package DEMOS;

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext};
/**
  * Created by ZJ on 2019-3-6
  * comment:
  */
        object GroupFavTeacher1 {
          def main(args: Array[String]): Unit = {
            val topN = 3;

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

            //分组排序:(按照学科)
            val grouped: RDD[(String, Iterable[((String, String), Int)])] = reducer.groupBy((t:((String, String), Int))=>t._1._1,1)

            //经过分组，获取学科迭代器，将每个学科组进行操作
            //因为一个学科的数据以及在一台机器的一个scala的集合里了
            val sorted: RDD[(String, List[((String, String), Int)])] = grouped.mapValues(_.toList.sortBy(_._2).reverse.take(topN))

            //收集结果
            val r: Array[(String, List[((String, String), Int)])] = sorted.collect()

            println(r.toBuffer)
            sc.stop()
          }

        }
