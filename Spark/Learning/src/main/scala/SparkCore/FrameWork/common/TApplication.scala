package SparkCore.FrameWork.common

import SparkCore.FrameWork.controller.WordCountController
import SparkCore.FrameWork.util.EnvUtil
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by ZJ on 2021/2/5
  * comment:启动模板
  */
trait TApplication extends App {

  //将代码逻辑抽取出来作为参数
  def start(master:String="local[*]",appName:String="Test")(logicCode : => Unit): Unit = {

    //创建spark的配置(设置本地setMaster("local[*]"))
    val conf: SparkConf = new SparkConf().setAppName(appName).setMaster(master)
    //创建spark的执行入口
    val sc = new SparkContext(conf)

    EnvUtil.put(sc)

    try {
      logicCode
    }catch {
      case ex => println(ex.getMessage)
    }

    //释放资源
    sc.stop()

    EnvUtil.clear()
  }
}
