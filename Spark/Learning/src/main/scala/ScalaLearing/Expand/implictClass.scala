package Expand

import java.io.{BufferedReader, File, FileReader}

import scala.io.Source

object implictClass {

  //柯里化
  def add(a: Int)(implicit b: Int) = a + b

  //隐式方法参数有多个的话只需要使用一个implict，必须放在方法参数的最后
  def addPlus(a: Int)(implicit b: Int, c: Int) = a + b + c

  //隐式函数(可定义多个)
  implicit def double2Int(double: Double): Int = double.toInt

  //隐式匿名函数
  implicit val fdouble2Int = (double: Double) => double.toInt

  implicit def file2Richfile(file : File ) = new RichFile(file)


  //隐式转换
  def main(args: Array[String]): Unit = {

    /**
      * 隐式参数：say方法的参数是隐式参数，如果没有传递参数的话
      * 编译器再编译时会自动获取上下文的隐式值
      */
    implicit val msg1 = "zj1"
    //implicit val msg2 = "zj2"
    say

    implicit val a: Int = 9
    println(add(5))

    println("======分割线======")
    /**
      * 隐式类型转换
      */
    val age: Int = 10.5

    //让file类具有count方法
    val file = new File("src/main")
    file.count()
    file.read

    println("======分割线======")

    /**
      * 隐式类，只能在单例，静态对象中使用
      */
    implicit class FileRead(file: File){
      def read = Source.fromFile(file).mkString
    }
  }

  def say(implicit a: String): Unit = {
    println(s"i say $a")
  }

}

class RichFile(file: File) {
  def count() = {
    val ff = new FileReader(file)
    val reader = new BufferedReader(ff)

    var sum = 0

    try {
      var line:String = reader.readLine()
      while (line != null) {
        line = reader.readLine()
        sum += 1
      }
    } catch {
      case _ => sum
    }finally {
      ff.close()
      reader.close()
    }
  }
}
