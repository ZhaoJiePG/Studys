package Basic

object 默认参数值函数 {
  def main(args: Array[String]): Unit = {
    //定义一个默认参数方法
    def add(a:Int=1,b:Int=2): Int ={
      a+b
    }
    // 等价于add(a=3,b=2)
    add(3)
    // 等价于add(a=4,b=5)
    add(4,5)
    // 等价于add(a=1,b=5)
    add(b=5)

  }
}
