package OOP

/**
  * Scala 里的类型，除了在定义 class,trait,object 时会产生类型，还可以通过 type 关键字来声明 类型。
  * type 相当于声明一个类型别名：
  */
object type关键字 {
  def main(args: Array[String]): Unit = {
    type S = String
    var name: S = "Oliver"
    println(name)
  }

  val b = new B()
  val c = new C()
  b.fn(3)
  c.fn("hello")
}

//通常 type 用于声明某种复杂类型，或用于定义一个抽象类型
class A {
  type T

  def fn(t: T): Unit = {
    println(t)
  }
}
class B extends A{
  override type T = Int
}
class C extends A{
  override type T = String
}