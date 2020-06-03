package OOP

/**
  * ScalaTrait(特质) 相当于 Java 的接口，实际上它比接口还功能强大。 与接口不同的是，它还可以定义属性和方法的实现。
  * 一般情况下 Scala 的类只能够继承单一父类，但是如果是 Trait(特质) 的话就可以继承多个，实现了多重继承。使用的关键字是 trait。
  */
object Trait {
  def main(args: Array[String]): Unit = {
    ScalaTraitImpl.sayHello("Oliver")
    // 如果ScalaTrailImpl没有重写small方法，则调用ScalaTrail中已经实现了的方法
    // 如果ScalaTrailImpl重写了small方法，则调用的是ScalaTrailImpl中的方法
    ScalaTraitImpl.small("wang")
    // output:
    // hello Oliver
    // wang like small

  }
}

trait ScalaTrait {
  //定以一个熟悉
  var pro: Int = 666

  //定以一个没有实现的方法
  def sayHello(name: String)

  //定以一个具体实现的方法
  def small(name:String): Unit ={
    println(s"太阳队${name}笑")
  }

  //混入特质
  private val student: Student with ScalaTrait = new Student with ScalaTrait {
    override def sayHello(name: String): Unit = println(s"name:${name}")
  }
  println(student.sayHello("zj"))
}

object ScalaTraitImpl extends ScalaTrait{
  // 实现方法时可以有override关键字，也可以没有
  def sayHello(name: String): Unit = {
    println(s"hello $name")
  }
  // 重写方法时必须得有override关键字
  override def small(name: String): Unit = {
    println(s"$name like small")
  }
}

class Student {

}