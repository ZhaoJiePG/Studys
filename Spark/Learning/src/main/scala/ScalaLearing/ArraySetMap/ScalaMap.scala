package ArraySetMap

import scala.collection.mutable.HashMap

object ScalaMap extends App {
  val map = Map("a" -> 1,"b"->2)
  val r = map("d")

  map.getOrElse("d",0)

  var map1 = new HashMap[String, Int]()

  map1("spark") = 1
  map1 += (("hadoop", 2))
  map1.put("storm", 3)
  println(map1)

  //取值
  map1.getOrElse("spark", 2)

  //map移除
  map1 -= "saprk"
  map1.remove("hadoop")
}
