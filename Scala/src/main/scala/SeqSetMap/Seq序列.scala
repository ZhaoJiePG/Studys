package SeqSetMap

import scala.collection.mutable.ListBuffer

/**
  * 不可变的序列 importscala.collection.immutable._
  *
  * 在 Scala 中列表要么为空（Nil 表示空列表）要么是一个 head 元素加上一个 tail 列表。 9::List(5,2) :: 操作符是将给定的头和尾创建一个新的列表
  */
object Seq序列 {
  def main(args: Array[String]): Unit = {
    // 创建一个不可变集合
    val list=List(1,2,3)
    // 创建一个可变集合
    val mutalist=ListBuffer(1,2,3)
    // 将0插入到list前面生成一个新的list
    val list1: List[Int] = list.:+ (0)
    val list2: List[Int] = 0+:list
    val list3: List[Int] = 0::list
    val list4: List[Int] = list.::(0)
    // 将一个元素添加到list后面，形成新的list5
    val list5: List[Int] = list:+(4)

    val list6=List(4,5,6)
    // 将两个list合并成一个新的list
    val list7: List[Int] = list++list6
    val list8: List[Int] = list ++ list6
    // 将list6插入到list前面形成一个新的集合
    val list9: List[Int] = list.:::(list6)
    list8.foreach(println)

  }
}
