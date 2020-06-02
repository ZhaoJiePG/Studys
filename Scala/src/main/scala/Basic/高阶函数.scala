package Basic

object 高阶函数 {
  def main(args: Array[String]): Unit = {
    //  高阶函数将其他函数作为参数
    def apply(f:Int => Int,p:Int): Unit ={
      print(f(p))
    }

    def fn1(a:Int): Int ={
      a*a
    }
    apply(fn1,10)/*output：100*/

  }
}
