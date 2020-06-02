package Basic

object 部分参数应用函数 {
  def main(args: Array[String]): Unit = {
    //定义了一个求和函数
    def sum(a:Int,b:Int): Int ={
      a+b
    }

    // 调用sum函数的时候，传入了一个参数a=10，但是b为待定参数。
    // sumWithTen形成了一个新的函数，参数个数为一个，类型为int型，
    def sumWithThen:Int => Int=sum(10,_:Int)

    //sumWithTen(1),本质是sum(10,1)
    println(sumWithThen(1))
  }
}
