package OOP

object apply方法 {
  def main(args: Array[String]): Unit = {
    println(apply方法(1,2))
  }

  def apply(a: Int, b: Int): Int = {
    a + b
  }
}
