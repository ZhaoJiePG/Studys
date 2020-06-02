package SeqSetMap

import scala.collection.immutable.HashSet
import scala.collection.mutable

object Set集合 {
  def main(args: Array[String]): Unit = {
    //可变的set
    // 创建一个可变set
    val set: mutable.HashSet[Int] = new mutable.HashSet[Int]()
    // 往set中加入元素1
    set+=1
    // add等价于+=
    set.add(2)
    // 添加另一个set中元素
    set ++= Set(7,8,9)
    // 移除一个元素
    set-=2
    // remove等价于-=
    set.remove(2)

    set.foreach(print)


    //不可变集合
    // 创建一个不可变set
    val set2: HashSet[Int] = new HashSet[Int]()
    // 将元素和原来的set合并生成一个新的set2，原有set不变
    val set3=set+1
    // 两个set合并生成一个新的set
    val set4=set++Set(4,5,6)
    set4.foreach(print)

  }
}
