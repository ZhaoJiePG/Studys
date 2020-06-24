package ArraySetMap

import scala.collection.immutable.HashSet

object ScalaSet extends App {

  //不可变set
  val set1 = new HashSet[Int]()

  val set2 = set1 + 4

  //set元素不能重复
  val set3 = set1 ++ Set(5,6,7)
  val set0 = Set(5,6,7) ++ set1
  println(set0.getClass)

  //可变set
  import scala.collection.mutable._


}
