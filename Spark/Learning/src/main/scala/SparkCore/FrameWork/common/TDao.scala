package SparkCore.FrameWork.common

import SparkCore.FrameWork.util.EnvUtil
import org.apache.spark.SparkContext

/**
  * Created by ZJ on 2021/2/5
  * comment:
  */
trait TDao {
  def readFile(path:String) ={
    val sc : SparkContext = EnvUtil.get()
    sc.textFile(path, 1)
  }
}
