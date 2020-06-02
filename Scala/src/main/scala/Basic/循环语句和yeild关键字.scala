package Basic

object 循环语句和yeild关键字 {
  def main(args: Array[String]): Unit = {
    //定义一个数组
    var arr = Array(1,2,3,4,5)

    //遍历数组中的每个元素
    for (ele <- arr){
      println(s"ele:$ele")
    }

    // 0 to 5 =>会生成一个范围集合Range（0,1,2,3,4,5),左闭右闭
    for(i <- 0 to 5){
      println(s"ele:$i") /*output:0,1,2,3,4,5*/
    }

    // 0 until 5 =>会生成一个范围集合Range（0,1,2,3,4)，左闭右开
    for(i <- 0 until 5){
      println(i) /*output:0,1,2,3,4*/
    }

    // for循环中可以增加守卫，下面这段语句是打印arr数组中的偶数
    for(i <- arr if i%2==0){
      print(i)
    }/*input:2,4,6*/

    //双层for循环
    for (i <- 1 to 3;j <- 1 to 3 if i!=i){
      println(i*10+j+"")
    }/*output:12,13,21,23,31,32*/

    println("===================")
    //  yield 关键字将满足条件的e的值又组合成一个数组
    var arr2: Array[Int] =for(e <- arr if e%2==0) yield e
    for (i <- arr2){
      println(i)/*output:2,4,6*/
    }
  }
}
