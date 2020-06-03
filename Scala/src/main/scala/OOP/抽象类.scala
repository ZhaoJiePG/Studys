package OOP

/**
  * 使用 abstract 修饰的类称为抽象类. 在抽象类中可以定义属性、未实现的方法和 具体实现的方法。
  */
object 抽象类 {

}
abstract class Animal{
  // 定义了一个属性
  var name:String="animal"
  // 定义一个未实现方法
  def sleep()
  // 定义一个带具体实现方法
  def eat(f:String): Unit ={
    println(s"eating $f")
  }
}