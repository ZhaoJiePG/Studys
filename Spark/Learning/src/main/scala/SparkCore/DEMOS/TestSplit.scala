package DEMOS

import java.io.{BufferedReader, File, FileInputStream, FileReader}
;

/**
  * Created by zx on 2017/10/8.
  */
object TestSplit {

  def main(args: Array[String]): Unit = {


    val line = "http://bigdata.edu360.cn/laozhao"

    //学科，老师
//    val splits: Array[String] = line.split("/")
//
//    val subject = splits(2).split("[.]")(0)
//
//    val teacher = splits(3)
//    println(subject + " "+ teacher)

    val index: Int = line.lastIndexOf("/")
    val teacher: String = line.substring(index + 1)
    println(teacher)

    val subject = line.substring(0,index).split("[.]")(0).split("/")(2)
    println(subject)


  }
}
