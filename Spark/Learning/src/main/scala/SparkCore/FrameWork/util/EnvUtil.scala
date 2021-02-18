package SparkCore.FrameWork.util

import org.apache.spark.SparkContext

/**
  * Created by ZJ on 2021/2/5
  * comment:threadlocal存储变量
  */
object EnvUtil {
  private val scLocal = new ThreadLocal[SparkContext]()

  def put(sc : SparkContext): Unit ={
    scLocal.set(sc)
  }

  def get(): SparkContext ={
    scLocal.get()
  }


  def clear() ={
    scLocal.remove()
  }
}
