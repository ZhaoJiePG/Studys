package SparkCore.FrameWork.application

import SparkCore.FrameWork.common.TApplication
import SparkCore.FrameWork.controller.WordCountController
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
  * Created by ZJ on 2021/2/5
  * comment:主函数入口
  */
object WordCountApplication extends App with TApplication{

  //启动应用程序
  start () {
    val controller = new WordCountController()

    controller.execute()
  }

}
