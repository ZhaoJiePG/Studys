package Basic

/*
 *scala的
 *      传名调用( call-by-name)
 *      传值调用( call-by-value)
 */
object 传值调用与传名调用 {
  def main(args: Array[String]): Unit = {
    def currentTime():Long={
      System.nanoTime()
    }

    //该方法的参数为一个无参的函数，并且函数的返回值为Long
    def delayd(f: =>Long):Unit={
      println(s"time = ${f}")
    }

    def delayed2(time: Long): Unit ={
      print(s"time=${time}")
    }

    delayd(currentTime())

    // 调用方式二
    // 等价于调用方式一，本质是先计算出参数的值，在带入方法
    var time: Long =currentTime()
    delayed2(time)
  }
}
