package ArraySetMap

object ScalaList extends App {

  //创建可变和不可变数组
  val list1 = List(1, 2, 3)

  //插入list数据
  val list2 = 0 :: list1
  val list3 = list1.::(0)
  val list4 = 0 +: list1
  val list5 = list1.+:(0)

  //将一个元素添加到list1后形成一个新集合
  val list6 = list1 :+ 3
  val list0 = List(4, 5, 6)
  val list7 = list1 ++ list0
  val list8 = list1 ++: list0
  val list9 = list1.:::(list0)
  //list为右结合

  import scala.collection.mutable._

  val lst0 = ListBuffer[Int](1, 2, 3)

  //创建一个可变数组
  val lst1 = new ListBuffer[Int]

  lst1 += 4
  lst1.append(5)

  lst1 ++= lst0

  val lst2 = lst0 ++ lst1

  val lst3 = lst0 :+ 5
}
