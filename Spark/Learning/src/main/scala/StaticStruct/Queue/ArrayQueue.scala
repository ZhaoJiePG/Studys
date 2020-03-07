package Queue


/**
  * Created by ZJ on 2019-4-1
  * comment:
  */
object ArrayQueue {
  def main(args: Array[String]): Unit = {
    //初始化一个队列
    val queue = new ArrayQueue(3)
    var key = ""
    while (true) {
      println("show:表示显示队列")
      println("exit:表示推出程序")
      println("add:输入队列一个属")
      println("get:取出队列数据")
      println("head:查看队列头数据，不改变队列")
      println()

      import scala.io.StdIn
      key = StdIn.readLine()
      key match {
        case "show" => queue.showQueue()
        case "exit" => System.exit(0)
        case "add" => {
          val value = StdIn.readInt()
          queue.addQueue(value)
        }
        case "get" => {
          val res = queue.getQueue()
          res match {
            case x:Exception => println(x.getMessage)
            case y:Int => println(s"取出的数是 $y")
          }
        }
        case "head" =>{
          var res = queue.headQueue()
          res match {
            case x:Exception => println(x.getMessage)
            case y:Int => println(s"取出的数是 $y")
          }
//          if(res.isInstanceOf[Exception]){
//            println(res.asInstanceOf[Exception].getMessage)
//          }else{
//            println("队列头元素为=" + res)
//          }
        }
      }
    }
  }
}

//使用数组模拟队列
class ArrayQueue(arrMaxSize: Int) {
  val maxSize = arrMaxSize

  //该数组用于存放数据，模拟队列
  val arr = new Array[Int](maxSize)

  //指向队列的头部,front指向队列数据的前一个位置()
  var front = -1
  //指向队列的尾部,rear指向队列的最后一个数据(含)
  var rear = -1

  //判断队列是否满
  def isFull(): Boolean = {
    rear == maxSize - 1
  }

  //判断队列是否为空
  def isEmpty(): Boolean = {
    rear == front
  }

  //添加数据到队列
  def addQueue(n: Int)={
    //判断是否满
    if(isFull()){
      println("队列满，无法加入...")
    }
    //先让rear后移
    rear += 1
    arr(rear) = n
  }

  //取数据
  def getQueue(): Any ={
    if(isEmpty()){
      return new Exception("队列空")
    }
    front += 1
    return arr(front)
  }

  //显示队列的所有数据
  def showQueue(): Unit = {
    if (isEmpty()) {
      println("队列空的，没有数据。。。")
      return
    }
    for (i <- front + 1 to rear) {
      printf("arr[%d]=%d\n", i, arr(i))
    }
  }

  //查看头元素,但是不改变数据
  def headQueue(): Any ={
    if (isEmpty()) {
      return new Exception("队列空")
    }
    //不改变front的值
    return arr(front+1)
  }
}
