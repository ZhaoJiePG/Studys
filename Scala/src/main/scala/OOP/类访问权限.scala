package OOP

/*
 * private 加在主构造器之前，这说明该类的主构造器是私有的，外部对象或者外部类不能访问
 * 也适用与辅助构造器
 */
object 类访问权限 {

}

class Student2 private(var name: String, val age: Int) {
  var gender: String = _

  private def this(name: String, age: Int, gender: String) {
    this(name, age)
    this.gender = gender
  }
}

//成员变量的访问权限
/*
 * private val age
 * age 在本类中有setter和getter方法
 * 但是加上private 也就意味着age只能在这个类的内部及其伴生类中可以修改
 */
class Student3 private(){
  private var name:String=_
  // 伴生类可以访问
  private var age:Int=_
  // private [this]关键字标识给属性只能在类内部访问，伴生类不能访问
  private [this] var gender:String="man"
}
// 伴生类
object Student3{
  def main(args: Array[String]): Unit = {
    val student = new Student3()
    // 伴生对象可以访问
    student.name="jack"
    student.age =20
    println(s"name=${student.name},age=${student.age}")
    // 伴生类不能访问
    //println(student.gender)
    // output:
    // name=jack,age=20

  }
}

//类包的访问权限

/*
 * private [this] class放在类声明最前面，是修饰类的访问权限，也就是说类在某些包下可见或不能访问
 * private [sheep] class代表该类在sheep包及其子包下可见，同级包不能访问
 */
private [this] class Student4 (val name:String,private var age:Int){
  var gender :String=_
}
// 伴生类
object Student4{
  def main(args: Array[String]): Unit = {
    val s = new Student4("JackMa",18)
    print(s.age)
  }
}