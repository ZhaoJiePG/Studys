package Basics

//Scala输出格式
object ScalaPrint extends App {
  val name: String = "雅迪科技"
  val price: Double = 990.22
  val url = "www.baidu.com"

  //普通输出
  println("name=" + name, "price=" + price, "url= " + url)

  /**
    * 文字‘f’插值器允许创建一个格式化的字符串，类似C语言的printf
    * 使用‘f’插值器，需要使用printf央视格式说明符号，如%d%f%i
    * 这里$name%s打印String变量James和$height%2.2f打印浮点值1.90.
    */
  println(f"$name 学费 $price,网址$url")
  printf("%s 学费 %1.2f,网址是%s", name, price, url)

  /**
    * 's'允许在处理字符串时直接使用变量
    * 在println语句中将String变量附加到普通字符串中
    */
  println(s"naem=$name,price=$price,url=$url")

  //字符串插入器可以处理任意字符串，还可以插入代码块
  println(s"1 + 1 = ${1 + 1}")

}
