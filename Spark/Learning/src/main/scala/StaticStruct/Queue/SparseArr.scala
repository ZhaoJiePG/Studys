package Queue


/**
  * Created by ZJ on 2019-4-1
  * comment:
  */
object SparseArr extends App{
  //行数和列数
  val rowSize = 11
  val colSize = 11

  //演示一个稀疏数组的使用
  val chessMap = Array.ofDim[Int](rowSize,colSize)

  //初始化地图
  chessMap(1)(2) = 1 //1表示黑子
  chessMap(2)(3) = 2 //2表示白字

  //输出原始的地图
  for(item <- chessMap){
    for(item2<-item){
      printf("%d\t",item2)
    }
    println()
  }

  //将chessMap转换成稀疏数组
  //思路 => 效果是达到对数据的压缩
  //class Queue.Node(row,col,value)
  //ArrayBuffer
  import scala.collection.mutable.ArrayBuffer
  val spareseArr = new ArrayBuffer[Node]()

  //获取数组的最后一个数组界限
  val node = new Node(rowSize,colSize,0)
  spareseArr.append(node)

  //遍历原数组
  for(i<- 0 until chessMap.length){
    for(j<- 0 until chessMap(i).length){

      //保存不为0的数据
      if(chessMap(i)(j) != 0){
        //构建一个Node
        val node = new Node(i,j,chessMap(i)(j))

        //加入导稀疏数组
        spareseArr.append(node)
      }
    }
  }

  //输出稀疏数组
  for(node <-spareseArr){
    printf("%d\t%d\t%d\n",node.row,node.col,node.value)
  }

  //恢复，存盘[]

  //读盘

  //稀疏数组 -> 原始数组
  //1.读取稀疏数组的第一个节点
  val newNode = spareseArr(0)
  val rowSize2 = newNode.row
  val colSize2 = newNode.col

  //创建二维数组
  val chessMap2 = Array.ofDim[Int](rowSize2,colSize2)

  //存入数据
  for(i <- 1 until spareseArr.length){
    val node = spareseArr(i)
    chessMap2(node.row)(node.col)=node.value
  }

  println("-------从稀疏数组恢复后的地图--------")
  for(item <- chessMap2){
    for(item2 <- item){
      printf("%d\t",item2)
    }
    println()
  }

}

class Node(val row:Int,val col:Int,val value:Int)