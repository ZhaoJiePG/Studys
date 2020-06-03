package MatchCase

object 匹配集合 {
  def main(args: Array[String]): Unit = {
    def listMatch(list:Any)=list match {
      case 0::Nil=>println("只有0元素的集合")
      case 7::9::Nil=>println("只有7和9两个元素的集合")
      case x::y::z::Nil=>println(s"只有三个元素集合${x},${y},${z}")
      case m::n=>println(s"拥有head和tail的集合。head:${m},tail:${n}")
    }
    listMatch(List(0))
    listMatch(List(7,9))
    listMatch(List(1,2,3))
    listMatch(List(8,7,6,5,4))
    // output:
    // 只有0元素的集合
    // 只有7和9两个元素的集合
    // 只有三个元素集合1,2,3
    // 拥有head和tail的集合。head:8,tail:List(7, 6, 5, 4)
  }
}
