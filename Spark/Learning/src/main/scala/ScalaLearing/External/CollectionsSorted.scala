package External

/**
  * Created by ZJ on 2019-6-25
  * comment:对Scala中集合(Collections)进行排序
  */
object CollectionsSorted {

  def main(args: Array[String]): Unit = {
    val res1: Unit = sorted1()
  }

  def sorted1(): Unit = {
    val s = List("a", "b", "F", "B", "e")
    val n = List(3, 7, 2, 1, 5)
    val m = Map(
      -2 -> 5,
      2 -> 6,
      5 -> 9,
      1 -> 2,
      0 -> -16,
      -1 -> -4
    )
    println(s.sortWith(_.toLowerCase<_.toLowerCase()))
    println(m.toList.sortBy(_._2))
  }
}
