package OOP

/*
 * scala中，类并不用声明为public
 * 如果你没有定义构造器，类会有一个默认的空参构造器
 *
 * var 修饰的变量，对外提供setter、getter方法
 * val 修饰的变量，对外只提供getter方法，不提供setter方法
 */
object 类定义 {
  def main(args: Array[String]): Unit = {
    val student = new Student
    student.name = "zj"
    //  错误代码，类中使用val修饰的变量不能更改
    //  student.age=20
    println(s"name = ${student.name},age = ${student.age}")

    val s1 = new Student1("Tom",18)
    println(s"name=${s1.age},age=${s1.age},gender=${s1.gender}")
    val s2 = new Student1("JackMa",20,"man")
    println(s2.gender)

  }
}

class Student {
  // _表示一个占位符，编译器会根据你变量的类型赋予相应的初始值
  var name: String = _
  // 错误代码，val修饰的变量不能使用占位符
  // val age:Int=_
  val age: Int = 10
}

//定义在类后面的为类主构造器, 一个类可以有多个辅助构造器
/*
 * 定义在类名称后面的构造器为主构造器
 * 类的主构造器中的属性会定义成类的成员变量
 * 类的主构造器中属性没有被var|val修饰的话，该属性不能访问，相当于对外没有提供get方法
 * 如果属性使用var修饰，相当于对外提供set和get方法
 */
class Student1(var name: String, val age: Int) {
  var gender: String = _

  def this(name: String, age: Int, gender: String) {
    this(name, age)
    this.gender = gender
  }
}
