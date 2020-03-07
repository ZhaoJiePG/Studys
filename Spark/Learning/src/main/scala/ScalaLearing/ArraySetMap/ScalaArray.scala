package ArraySetMap

import scala.collection.mutable.ArrayBuffer

object ScalaArray extends App {

  //定义一个数组，固定长度
  var x: Array[String] = new Array[String](3)

  var y = new Array[String](3)

  //定长数组，内容可变，长度不可变
  val z = Array(1, 2, 3)

  //修改第0个元素
  z(0) = 100

  //变长数组
  val ab = ArrayBuffer[Int]()
  //+=向数组中追加数据
  ab += 1
  ab += (2,3,4,5)
  //++=数组
  ab ++= Array(6,7,8)
  ab ++= ArrayBuffer(9,10,11)

  //insert&remove
  ab.insert(0,-1,0)
  ab.remove(8,2)
  println(ab)

  //map|flatten|flatMap|foreach 方法的使用
  val array = Array[Int](2, 4, 6, 8, 9)

  val res1 = array.map((x: Int) => x * 2)
  val res2 = array.map(x => x * 2)
  val res3 = array.map(_ * 2)

  println(res3.toBuffer)

  println("==================================")

  val words = Array("hello tom hell jvm","hello kitty")

  //数组中没和元素按照空格进行切分
  val res4:Array[Array[String]] = words.map(_.split(" "))

  //对分割后的数据进行扁平化
  private val res5: Array[String] = res4.flatten

  val res6 = words.flatMap(_.split(" "))

  res6.foreach(println)

}
