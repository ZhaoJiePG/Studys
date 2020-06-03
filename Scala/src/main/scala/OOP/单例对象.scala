package OOP

/**
  * 在 Scala 中，是没有 static 这个东西的，但是它也为我们提供了单例模式的实现方法，那 就是使用关键字 object,object 对象不能带参数
  */
object 单例对象 {
  def saySomeThing(msg:String)={
    println(msg)
  }
}

object test{
  def main(args: Array[String]): Unit = {
    单例对象.saySomeThing("aaa")
    println(单例对象)
    println(单例对象)
  }
}