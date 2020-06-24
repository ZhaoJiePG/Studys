package ArraySetMap

object ScalaTuple {

  //定义元祖
  var t = (1,"hello",true)

  val tuple1 = new Tuple3(1,"hello",true)

  println(t._3)

  t.productIterator.foreach(println)

  val tuple2 = (1,3)

  //交换位置
  val swap = tuple2.swap
}
