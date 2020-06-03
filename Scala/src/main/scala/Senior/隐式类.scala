package Senior

object 隐式类 {
  def main(args: Array[String]): Unit = {

    implicit class Calulator(x: Int) {
        def add(y:Int): Int =x+y
    }
    var z:Int = 5
    println(z.add(3))
  }
}
