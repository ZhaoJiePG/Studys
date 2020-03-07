package OOP

//测试单列对象
object SingleClass {

  //Object里的都是静态方法
  def saySomething(msg: String): Unit = {
    println(msg)
  }
}

object test1 extends App {
  SingleClass.saySomething("滚犊子")

  println(SingleClass)

  println(SingleClass)
}
