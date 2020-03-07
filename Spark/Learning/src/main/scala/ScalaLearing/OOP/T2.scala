package OOP

trait T2 {
  val className = "zj"

  def teachSay(name :String)

  def doSoming(): Unit ={
    println("dsdsdsd")
  }

}
abstract class Animal{
  val name = "animal"

  def sleep()

  def eat(f:String): Unit ={
    println(s"$f")
  }
}
class Dog extends Animal{
  override def sleep(): Unit = {
    println("dsdsd")
  }
}
