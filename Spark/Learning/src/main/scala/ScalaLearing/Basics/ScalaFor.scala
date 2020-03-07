package Basics

//Scala for 循环
object ScalaFor extends App {

  //循环遍历数组
  private val array: Array[Int] = Array(1, 2, 3, 4, 5, 6)
  for (ele <- array) {
    println(ele)
  }

  //通过角标器获取数组里的元素
  for (i <- 0 to 5) {
    println(array(i))
  }

  //打印数组里的偶数
  for (e <- array if e % 2 == 0) {
    println(e)
  }

  //prac
  for (i <- 1 to 3; j <- 1 to 3 if i != j) {
    println((10 * i + j) + "")
  }

  //yield构造并返回与给定集合相同类型的集合
  val res: Array[Int] = for (e <- array if e % 2 == 0) yield e

}
