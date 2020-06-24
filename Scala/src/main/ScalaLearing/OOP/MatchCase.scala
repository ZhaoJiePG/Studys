package OOP

import scala.util.Random

//模式匹配
object MatchCase extends App {

  //匹配字符串，类型，首位
  val arr = Array("aa", "bb", "cc")
  val i = Random.nextInt(arr.length)
  println(i)
  val name = arr(i)
  println(name)
  name match {
    case "aa" => println("11111")
    case "bb" => println("22222")
    case _ => println("dsdsds")
  }
  println("--------------------------")

  val arr1 = Array("hello123", 1, 2.0, Student3, 2L)
  val elem = arr1(3)
  elem match {
    case x: Int => println("Int" + x)
    case y: Double if (y > 0) => println("Double" + y)
    case z: String => println("String" + z)
    case Student3 => println()
  }
  println("--------------------------")

  //匹配数组
  val arr3 = Array(1, 1, 7, 0, 2, 3)
  arr3 match {
    case Array(0, 2, x, y) => println(x + "" + y)
    case Array(2, 1, 7, y) => println("only 0" + y)
    case Array(1, 1, 7, _*) => println("0....")
    case _ => println("null")
  }
  println("--------------------------")

  //匹配集合
  val lst = List(0, 3, 4)
  lst match {
    case 0 :: Nil => println("only 0")
    case x :: y :: Nil => println(s"x $x y $y")
    case 0 :: a => println(s"value : $a")
    case _ => println("null")
  }
  println("--------------------------")

  //匹配元组
  val tup = (1, 2, 3)
  tup match {
    case (3, x, y) => println(x + y)
    case (z, x, y) => println(x + y + z)
    case (_, w, 5) => println(w)
  }
  println("--------------------------")

  //匹配样例类/样例对象
  case class qq(id: String, name: String)

  case class ww(time: Long)

  case object ee

  val arrr = Array(ee, new ww(444), new qq("zj", "18"))

  val o = Random.nextInt(arrr.length)
  val ele = arr(o)

  }
