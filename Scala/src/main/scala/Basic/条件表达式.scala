package Basic

object 条件表达式 {
  def main(args: Array[String]): Unit = {
    //if语句的使用
    var faceValue = 98
    var res1 = if (faceValue>90) "帅的一比" else "有点恼火"
    println(res1)

    //3>5 不成立，且代码没有else分支，那么res2应该输出什么呢？
    var i=3
    var res2=if (i>5) i
    println(res2)// output  ()代表空

    // 支持嵌套，if...else if ...else代码过多时可以使用{}
    val score=85
    if(score<60)"不及格"
    else if(score>=60&&score<70)"及格"
    else if (score>=80&&score<90)"优秀"
    else "优秀"
  }
}
