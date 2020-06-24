package ArraySetMap

object ScalaPar {

  val lst0 = List(1, 2, 3, 4, 5, 6, 7)

  val lst11 = lst0.par.fold(100)((x, y) => x + y)

  val lst12 = lst0.foldLeft(100)((x, y) => (x + y))

  val arr = List(List(1,2,3),List(4,5,6),List(7,8,9))

  val result = arr.aggregate(0)(_+_.sum,_+_)
}
