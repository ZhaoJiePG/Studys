package Basics
//Scala if 条件表达式
object ScalaIf extends App {

  //if语句的使用
  val faceValue = 90
  val res1 = if(faceValue > 90) "true" else "false"
  println(res1)

  //8>8 不成立，且代码有sele分支
  val i = 8
  val res2 = if(i>8) i
  println(res2)

  val res3 = if(i>8) i else "error"
  println(res3)

  //if .. else if .. else代码块多时使用代码块{}
  val score = 76
  val res4 = {
    if(score > 60 && score < 70) "及格"
    else if (score <=70 && score < 80)"良好"
    else "优秀"
  }
}
