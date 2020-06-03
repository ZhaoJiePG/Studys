package SeqSetMap

object 案例wordCount {
  def main(args: Array[String]): Unit = {
    // 第一种方式
    val words = Array("hello tom jack oliver", "tom jack jim hello")
    val strings: Array[String] = words.flatMap(_.split(" "))
    val tuples: Array[(String, Int)] = strings.map((_,1))
    val stringToTuples: Map[String, Array[(String, Int)]] = tuples.groupBy(_._1)
    val stringToInt: Map[String, Int] = stringToTuples.mapValues(_.length)

    //第二种方式
    val list: List[(String, Int)] = words.flatMap(_.split(" ")).map((_, 1))
      .groupBy(_._1).mapValues(_.length).toList
    list.foreach(println)

    //第三种方式
    val list2: List[(String, Int)] = words.flatMap(_.split(" ")).groupBy(x=>x).map(t => (t._1,t._2.length)).toList
  }
}
