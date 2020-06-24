package OOP

//设置构造器的访问权限
class Student2 private (val name:String,var age:Int){
  //private标识主构造器是私有的，外部无法直接访问这个构造器

  var gendar:String = _
  //在辅助构造器中必须先调用主构造器
  def this(name:String,age:Int,gendar:String){
    this(name,age)
    this.gendar=gendar
  }
}
object test2{
  def main(args: Array[String]): Unit = {
    val s1 = new Student2("laoy",18,"male")
    println(s"${s1.gendar}")
  }
}