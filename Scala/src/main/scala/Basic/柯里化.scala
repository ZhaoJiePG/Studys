package Basic

/**
  * 柯里化(Currying)指的是将原来接受两个参数的函数变成新的接受一个参数的函数的过程。新 的函数返回一个以原有第二个参数为参数的函数。
  */
object 柯里化 {
  def main(args: Array[String]): Unit = {
    //定义一个求和函数
    def add(a: Int, b: Int): Int = a + b

    def add2(a: Int)(b: Int): Int = a + b
    // 那么我们在调用的是后应该是add(1)(2),其结果还是等于3，这种方式（或过程）就叫做柯里化，
    // 经过柯里化之后函数通用性降低，但是适用性有所提高
    //分析下其演变过程
    def add3(a: Int): Int => Int = {
      (b: Int) => a + b
    }

    println(add2(1)(2))

    println(add3(1)(2))
  }

}
