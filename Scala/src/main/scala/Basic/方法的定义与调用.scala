package Basic
/*
 * 方法的定义及调用
 * 定义方法的格式为 :
 * def methodName ([listofparameters]) : [ returntype ] = { }
 * 如果不使用等号和方法体，则隐式声明抽象(abstract)方法 。
 */
object 方法的定义与调用 {
  def main(args: Array[String]): Unit = {
    // 定义一个sum方法，该方法有两个参数，返回值为int类型
    def sum(a:Int, b: Int): Int = { a + b }
    // 调用
    val result: Int =sum(1,5)
    println(result)
    // 该方法没有任何参数 , 也没有返回值
    def sayHello1(): Unit = println("Say BB1")
    def sayHello2(): Unit = println("Say BB2")
    sayHello1 // 如果方法没有()调用时不能加()
    sayHello2() // 可以省略( ) ,也可以不省略

  }
}
