package SeqSetMap

import scala.collection.parallel.immutable.ParSeq

//调用集合的 par 方法, 会将集合转换成并行化集合
object 并行化集合par {
  def main(args: Array[String]): Unit = {
    val list:List[Int] = List(1,7,9,8,0,3,5,4,6,2)
    val par: ParSeq[Int] = list.par
    println(par)
  }
}
