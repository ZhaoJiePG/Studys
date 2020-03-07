package Functions

/**
  * 定义方法以及调用
  * 格式：
  * def methmodName([List of parameters]):[return type]={}
  */
object ScalaMethod extends App {

  //定义sum方法，2个参数，返回int
  def sum(a: Int, b: Int): Int = {
    a + b
  }

  println(sum(1, 2))

  //改方法没有任何参数，也没有返回值
  def sayHello1 = println("Say BB1")

  def sayHello2() = println("Say BB2")

  sayHello1 //方法没有()调用不能省略
  sayHello2 //方法有，可省略

  //方法可转换为函数,方法名加 空格_
  val fm = sum _
  println(fm(2, 3))

  //匿名函数定义和调用
  val f1 = (x: Int) => x * 10

  val f2 = (x: Int, y: Int) => x * y

  val f4 = () => 1

  println(f1(2) + "" + f2(2, 3) + "" + f4())
}
