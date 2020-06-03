package OOP

/**
  * 继承是面向对象的概念，用于代码的可重用性。
  * 被扩展的类称为超类或父类, 扩展的类称 为派生类或子类。Scala 可以通过使用 extends 关键字来实现继承其他类或者特质
  */
/**
 * with 后面只能是特质
 * 父类未实现的方法，子类必须实现
 * 父类已经实现的方法，子类要重写该方法，必须使用override关键字
 */
object 继承 {
  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.sleep()
    dog.eat("a")
  }
}
abstract class Animal1{
  // 定义了一个属性
  var name:String="animal"
  // 定义一个未实现方法
  def sleep()
  // 定义一个带具体实现方法
  def eat(f:String): Unit ={
    println(s"eating $f")
  }
}

class Dog extends Animal1{
  override def sleep(): Unit = {
    println("耳朵贴地睡")
  }
}
