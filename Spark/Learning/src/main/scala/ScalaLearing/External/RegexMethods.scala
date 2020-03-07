package External

import scala.util.matching.Regex

/**
  * Created by ZJ on 2019-6-25
  * comment:Scala正则表达式
  */
object RegexMethods {

  def main(args: Array[String]): Unit = {

    //编写规则
    val regex: Regex = """([0-9]+) ([a-z]+)""".r

    //需要匹配的
    val line = "123 iteblog"

    //第一种方式
    val regex(num,blog) = line
    println(num+blog)

    //第二种方式
    line match {
      case regex(num,blog)=>println(num+"\t"+blog)
      case _ => println("ohh...")
    }

    //第三种方式
    val m: Regex.MatchIterator = regex.findAllIn(line)
    println(m.group(1) + "\t" + m.group(2))

  }
}
