package SeqSetMap

import scala.collection.mutable

/**
  * 在Scala中Option类型样例类用来表示可能存在或也可能不存在的值(Option的子类有Some 和 None)。Some 包装了某个值，None 表示没有值。
  */
object Map和Option {
  def main(args: Array[String]): Unit = {
    val map: mutable.HashMap[String, Int] = mutable.HashMap[String, Int](
      "a" -> 1,
      "b" -> 2,
      "c" -> 3
    )

    val i: Int = map("d")
    // map的get方法返回值Option，意味着maybeInt可能取到值也可能没有取到值
    val maybeInt: Option[Int] = map.get("d")
    // 如果maybeInt=None时会抛出异常
    val v = maybeInt.get
    // 第一个参数为要获取的key
    // 第二个参数为如果没有这个key返回一个默认值
    val ele: Int = map.getOrElse("d", -1)
  }
}
