package SparkCore.FrameWork.service

import SparkCore.FrameWork.common.Tservice
import SparkCore.FrameWork.dao.WordCountDao
import org.apache.spark.rdd.RDD

/**
  * Created by ZJ on 2021/2/5
  * comment:数据逻辑层 transform操作
  */
class WordCountService extends Tservice{
  private val wordCountDao = new WordCountDao()

  def dataAnalysis()={
    //指定以后从哪里读取RDD
    val lines: RDD[String] = wordCountDao.readFile("D:\\Maven\\Studys\\Spark\\Learning\\src\\main\\scala\\Datas\\ip.txt")

    //wordcount
    val rdd: RDD[(String, Int)] = lines.flatMap(_.split("|")).map((_, 1)).reduceByKey(_ + _)
      .sortBy(_._2, false)

    rdd

  }
}
