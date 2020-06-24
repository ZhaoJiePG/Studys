package Expand

import Expand.ClothesEnum.ClothesEnum

/**
  * 泛型：就是约束类
  */
object scalaFanxin extends App{

  def say(content: String) = {
    val clothe1: Clothes[ClothesEnum, String, Int] = new Clothes[ClothesEnum,String,Int](ClothesEnum.上衣,"black",90)
    println(clothe1.clothType)

    val clothe2: Clothes[ClothesEnum, String, String] = new Clothes[ClothesEnum,String,String](ClothesEnum.上衣,"black","M")
    println(clothe2.color)

  }

}

class Clothes[A,B,C](val clothType:A,val color: B,val size:C)

//枚举
object ClothesEnum extends Enumeration{
  type ClothesEnum = Value
  val 上衣,内衣,裤子 = Value
}

//列子
abstract class Message[T](context: T)
class StrMessage[String](context: String) extends Message[String](context)
class IntMessage[Int](context: Int) extends Message[Int](context)