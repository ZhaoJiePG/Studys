package Expand

/**
  * Created by ZJ on 2019-3-9
  * comment:上下界约束
  */
object UpperLowerBounds {
  def main(args: Array[String]): Unit = {

    val cmpIn = new CmpInt(Integer.valueOf(2), new Integer(9))
    println(cmpIn.bigger)

    //指定泛型，底层隐式转换
    val cmpIn1 = new CmpInt[Integer](1, 2)
    println(cmpIn1.bigger)

    //视图界限,不用指定泛型也可以隐式转换
    val cmpIn2 = new CmpComm(1, 2)
    println(cmpIn1.bigger)

    //比较对象
//    case class Studentt(name :String, age :Int)
//    val tom = Studentt("Tom",18)
//    val jim = Studentt("Jim",18)
//    val cmpIn3 = new CmpInt(tom, jim)
//    println(cmpIn3.bigger)

  }
}

//泛型上界约束
class CmpInt[T <: Comparable[T]](a: T, b: T) {

  def bigger = if (a.compareTo(b) > 0) a else b
}

//试图界定：view bounds，自动发生隐式转换
class CmpComm[T <% Comparable[T]](a: T, b: T) {

  def bigger = if (a.compareTo(b) > 0) a else b
}