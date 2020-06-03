package MatchCase

object 匹配字符串类型守卫 {
  def main(args: Array[String]): Unit = {
    /**
      * 匹配字符串
      */

    def contentMatch(str: String): Unit = str match {
      case "dog" => println("小狗")
      case "cat" => println("小猫")
      case "1" => println("数字1")
      case _ => println("匹配失败")
    }

    contentMatch("cat")
    contentMatch("1")
    contentMatch("2")
  }

  /**
    * 匹配类型
    */
  def typeMatch(ele: Any) = ele match {
    case x: Int => println(s"Int:S${x}")
    case y: Double => println(s"Double:S${y}")
    case z: String => println(s"String:S${z}")
    case _ => println("match failure")
  }

  typeMatch("hello")
  typeMatch(2)
  typeMatch(2d)
}
