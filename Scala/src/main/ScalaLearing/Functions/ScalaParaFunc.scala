package Functions

import java.util.Date

//函数参数应用
object ScalaParaFunc extends App {

  //可变参数方法
  def methodManyParams(a: String*): Unit = {
    for (p <- a) {
      println(p)
    }
  }

  methodManyParams("中华", "人民", "共和国")

  //带默认参数列表
  def add(a: Int=1, b: Int = 7) = {
    println(s"a+b=${a + b}")
    a + b
  }
  add(2)
  add(b=9,a=2)
  add(b=8)

  //部分参数应用函数
  def log(date:Date,message:String)={
    println(s"$date,$message")
  }

  val date = new Date()

  //调用log时，传递一个具体的时间参数，message为特定参数
  //logBoundDate生成一个新的函数，只有log的部分参数
  val logBoundDate :String =>Unit = log(date, _:String)

  logBoundDate("jerry")

}
