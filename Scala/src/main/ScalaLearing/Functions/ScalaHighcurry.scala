package Functions

//高阶函数 柯里化
import java.util.Date

object ScalaHighcurry extends App {

  //高阶函数：将其他函数作为参数或者其结果是函数的函数

  //定义一个方法，参数为带一个参数返回值类型的函数f和一个整形参数v，返回一个参数
  def apply(f: Int => String, v: Int) = f(v)

  //定义一个方法，参数为一个整形参数，返回类型为String
  def layout(x: Int) = "[" + x.toString + "]"

  println(apply(layout, 99))

  //柯里化
  def add1(x: Int, y: Int) = x + y

  def add2(x: Int)(y: Int) = x + y

  def add3(x: Int) = (y: Int) => x + y

  println(add1(1,2))
  println(add2(2)(3))
  println(add3(3)(4))

  //偏函数
  def func1 :PartialFunction[String,Int]= {
    case "one" => 1
    case "two" => 2
    case _ => -1
  }

  def func2(num:String) :Int = num match {
    case "one" => 1
    case "two" => 2
    case _ => -1
  }

  println(func1("one"))
  println(func1("two"))
}
