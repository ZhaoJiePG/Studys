package Basic

/**
  * Scala中的格式化输出
  */
object 字符串的格式化输出 {
  def main(args: Array[String]): Unit = {
    val name = "JackMa"
    val price = 998.88d
    val url = "www.baidu.com"
    // 普通输出,注意这里是可以使用逗号分隔的，但是在java中，我们是需要用“+”号拼接
    println("name=" + name,"price="+price,"url="+url)

    // 'f'插值器允许创建一个格式化的字符串，类似于C语言中的printf。
    // 在使用'f'插值器时，所有变量引用都应该是printf样式格式说明符，如％d ,％i ,％f等 。
    // 这里$name％s打印String变量name,
    println(f"姓名：$name%s,价格：$price%1.2f,网址：$url%s")
    println(f"姓名：%%s,价格：%%1.1f,网址：%%s",name,price,url)

    // 's'插值器允许在字符串中直接使用变量
    // 下列语句中将String型变量（$name）插入到普通字符串中
    println(s"name=$name,price=$price,url=$url")

    //'s'插值器还可以处理任意形式的表达式
    println(s"1+1=${1+1}") //output "1+1=2"
  }
}
