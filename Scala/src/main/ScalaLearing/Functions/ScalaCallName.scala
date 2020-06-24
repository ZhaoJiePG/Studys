package Functions

//scala的传名调用，传值调用
object ScalaCallName extends App {

  def currentTime() = {
    println("打印当前系统时间")
    System.nanoTime()
  }

  //该方法的参数为一个无参的函数，并且函数的返回值为Long
  def delayed(f: => Long) = {
    println("delayed ======")
    println("time = " + f)
  }

  def delayed1(time : Long) = {
    println("delayed ======")
    println("time = " + time)
  }

  //方式一
  delayed(currentTime)
  println("====================")

  //方式二
  val time = currentTime()
  delayed1(time)
}
