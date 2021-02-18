package SparkCore.FrameWork.controller

import SparkCore.FrameWork.common.TController
import SparkCore.FrameWork.service.WordCountService
import org.apache.spark.rdd.RDD

/**
  * Created by ZJ on 2021/2/5
  * comment:逻辑输出层 action操作
  */
class WordCountController extends TController{
  val wordCountService = new WordCountService()

  //调度
  def execute()={
    val array = wordCountService.dataAnalysis()
    array.foreach(println)

  }

}
