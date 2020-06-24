package OOP

class Student1(val name:String,var age:Int) {
  var gendar:String = _

  //辅助构造器，使用def this
  //在辅助构造器中必须先调用主构造器
  def this(name:String,age:Int,gendar:String){
    this(name,age)
    this.gendar=gendar
  }
}
object test11{
  def main(args: Array[String]): Unit = {
    val s = new Student1("zj",38)
    println(s"${s.name} ${s.age}")

    val s1 = new Student1("zj",44,"male")
    println(s"${s1.gendar}")
  }
}
