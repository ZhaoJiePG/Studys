package OOP

class Student {
  //scala中不声明构造器，会有默认的构造器

  var name: String = _
  //val name不能使用占位符

  val age: Int = 10

}
object Test{
  val name = "zhangsan"

  def main(args: Array[String]): Unit = {
    type s = String
    //调用空参构造器，可加可不加()
    val stu = new Student
    stu.name = "zj"

    //使用val修饰的变量bu不能更改
    println(s"name====${stu.name}：${stu.age}")
    println("Test name is ===== "+Test.name)

  }
}
