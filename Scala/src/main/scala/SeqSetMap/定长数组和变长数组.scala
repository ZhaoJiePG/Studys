package SeqSetMap

import scala.collection.mutable.ArrayBuffer

object 定长数组和变长数组 {
  def main(args: Array[String]): Unit = {
    // 初始化一个长度为8的定长数组,所有元素均为0
    val arr=new Array[Int](8)
    // 直接打印数组的到的是数组的hashcode值
    print(arr)
    // 将数组转换成数组缓冲，就可以看到数组中内容
    print(arr.toBuffer)
    // 定义一个长度为3的定长数组
    val arr2=Array("tom","jack","oliver")
    // 通过索引获取数组中的值
    println(arr2(2))
    // 创建一个变长数组
    var arr3: ArrayBuffer[Int] = ArrayBuffer[Int]()
    // 末尾追加1
    arr3 += 1
    //  追加多个元素
    arr3 += (2,3,4,5)
    //  追加一个数组
    arr3 ++= Array(6,7)
    //  追加一个变长数组
    arr3 ++= ArrayBuffer(8,9)
    //  打印
    arr3.foreach(println)
    // 在某个位置插入元素
    arr3.insert(0,-1,0)
    //  移除指定索引元素
    arr3.remove(0,1)
  }
}
