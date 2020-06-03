package Senior

object 隐式的转换类型 {
  def main(args: Array[String]): Unit = {
    //var i:Int=3.14/*此时程序会报错*/
    implicit def double2Int( d:Double)=d.toInt
    // i是int类型，但是赋值的时候缺是一个浮点型，此时编译器会在当前上下文中找一个隐式转换，
    // 找一个double转换成int的隐式转换
    var i:Int=3.14
    println(i)
  }
}
