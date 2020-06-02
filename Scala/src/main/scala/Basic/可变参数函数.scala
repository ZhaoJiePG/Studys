package Basic

/*
 *scala的可变参数
 */
object 可变参数函数 {
  def main(args: Array[String]): Unit = {
    //定义一个可变参数方法
    def methodManyParams(params: Int*): Unit ={
      for (i <-params){
        println(s"i = ${i}")
      }
    }

    // 调用
    methodManyParams(1,2,3,5)
  }
}
