package Basic
//被包在花括号内没有 match 的一组 case 语句是一个偏函数，它是 PartialFunction[A,B]的一个 实例，A 代表参数类型，B 代表返回类型，常用作输入模式匹配。
object 偏函数 {
  def main(args: Array[String]): Unit = {
    def fun:PartialFunction[String,Int]={
      case "one" => 1
      case "two" => 2
      case _ => -1
    }

    println(fun("one"))
  }
}
