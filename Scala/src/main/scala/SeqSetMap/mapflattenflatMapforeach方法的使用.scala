package SeqSetMap

object mapflattenflatMapforeach方法的使用 {
  def main(args: Array[String]): Unit = {
    val arr=Array(1,2,3,4,5)
    // map函数将arr数组中所有元素进行某种映射操作，
    // (x:Int)=>x*2为一个匿名函数，x就是数组中的每个元素
    val z= arr map((x:Int)=>x*2)
    // 或者这样写，编译器会推断数据的类型
    val y=arr.map(x=>x*2)
    // 亦或者，_表示入参，代表数组中每个元素
    val x=arr.map(_ * 2)

    println("--------骚气分割线-----------")

    val words=Array("tom jack oliver jack","hello tom oliver tom ")
    // 将words中元素按照“，”切分
    val splitWords: Array[Array[String]] = words.map(x=>x.split(","))
    //此时数组中的每个元素进行split操作之后变成了Array,
    // flatten是对splitWords里面的元素进行扁平化操作
    val flatten: Array[String] = splitWords.flatten

    // 上诉两步操作可以等价于flatmap，意味着先map操作之后进行flatten操作
    val result=words.flatMap(_ .split(","))

    // 遍历数组，打印每个元素
    result.foreach(print)

  }
}
