package OOP
/*
 * 样例类，使用case关键字修饰的类，其重要的就是支持模式匹配
 * 样例类：case class 类名（属性）
 * 类名定义必须是驼峰式,属性名称第一个字母小写
 */
object 样例类对象 {
  def main(args: Array[String]): Unit = {
    val message = new Message("","")
    println(message)
  }
}

case class Message(sender:String,massageContent:String)
