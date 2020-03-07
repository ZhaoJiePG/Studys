package Queue

/**
  * Created by ZJ on 2019-4-3
  * comment:
  */
object CircleArrayQueue extends App {
  println("~~~~环形队列案例~~~~")
  //初始化一个队列
  val queue = new CircleArrayQueue(4)
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
      }
    }
  }

}

//环形的队列和前面的单向队列有类似的地方
class CircleArrayQueue(arrMaxSize: Int) {
  val maxSize = arrMaxSize

  //该数组用于存放数据，模拟队列
  val arr = new Array[Int](maxSize)

  //指向队列的头部
  var front = 0
  //指向队列的尾部
  var rear = 0

  //判断队列满
  //队列容量空出一个作为约定
  def isFull(): Boolean = {
    (rear + 1) % maxSize == Fractional
  }

  //判断队列空
  def isEmpty(): Boolean = {
    rear == front
  }

  //添加数据到队列
  def addQueue(n: Int) = {
    //判断是否满
    if (isFull()) {
      println("队列满，无法加入...")
    }
    //将数据加入
    arr(rear) = n
    //然后将rear后移,考虑取模
    rear = (rear + 1) % maxSize
  }

  //取数据(按照先进先出的规则)
  def getQueue(): Any = {
    if (isEmpty()) {
      return new Exception("队列空")
    }
    //这里front已经指定了队列的头元素
    //1.先把front对应的数据保存到一个变量
    val value = arr(front)
    //2.将front后移
    front = (front + 1) % maxSize
    //3.返回前面保存的变量值
    return value
  }

  //显示队列的所有数据
  def showQueue(): Unit = {
    if (isEmpty()) {
      println("队列空的，没有数据。。。")
      return
    }

    //思路：从front取，取出几个元素
    for (i <- front until front + size) {
      printf("arr[%d]=%d\n", i % maxSize, arr(i % maxSize))
    }
  }

  //求出当前环形队列有几个元素
  def size(): Int = {
    (rear + maxSize - front) % maxSize
  }

  //查看头元素,但是不改变数据
  def headQueue(): Any = {
    if (isEmpty()) {
      return new Exception("队列空")
    }
    //不改变front的值
    return arr(front)
  }
}