package SparkCore.Methods

import com.alibaba.fastjson.JSON
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSONObject

object Practice {
  def main(args: Array[String]): Unit = {
    // 1. 初始化spark配置信息, 并建立到spark的连接
    val conf: SparkConf = new SparkConf().setAppName("Practice").setMaster("local[*]")
    val sc = new SparkContext(conf)
    // 2. 从文件中读取数据, 得到 RDD. RDD中存储的是文件的中的每行数据
    val lines: RDD[String] = sc.textFile("D:\\Maven\\Studys\\Spark\\Learning\\src\\main\\scala\\Datas\\agent.log")
    // 3. ((provice, ad), 1)
    val itemRDD: RDD[item1] = lines.map(item => {
      val strings: Array[String] = item.split(" ")
      val item2 = new item1(strings(0).toDouble, strings(1), strings(2), strings(3), strings(4))
      item2
    })

    val provinceADAndOne: RDD[((String, String), Int)] = itemRDD.map(item => {
      ((item.province, item.advers), 1)
    })
    // 4. 计算每个省份每个广告被点击的总次数
    val provinceADSum: RDD[((String, String), Int)] = provinceADAndOne.reduceByKey(_+_)
    //.collect().foreach(println)
    // 5. 将省份作为key，广告加点击数为value： (Province,(AD,sum))
    val provinceToAdSum: RDD[(String, (String, Int))] = provinceADSum.map(item =>(item._1._1,(item._1._2,item._2)))
    // 6. 按照省份进行分组
    val provinceGroup: RDD[(String, Iterable[(String, (String, Int))])] = provinceToAdSum.groupBy(_._1)
    //7. 对同一个省份的广告进行排序, 按照点击数的降序
    val result: RDD[(String, List[(String, (String, Int))])] = provinceGroup.mapValues(item => {
      item.toList.sortBy(_._2).take(3)
    })
    //8. 按照省份的升序展示最终结果
    result.sortBy(_._1).collect.foreach(println)
    sc.stop()

  }
}
case class item1(timestamp:Double,province:String,city:String,user:String,advers:String)