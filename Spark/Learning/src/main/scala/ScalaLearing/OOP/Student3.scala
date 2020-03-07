package OOP

/**
  * 成员变量的访问权限
  * private age意味age只能在此类和半生对象访问
  */
class Student3 (val name:String,var age:Int){
  var gendar:String = _
  //在辅助构造器中必须先调用主构造器
  def this(name:String,age:Int,gendar:String){
    this(name,age)
    this.gendar=gendar
  }

  private[this] val province:String = "无锡市"

  def getAge = 18
}
object Student3{
  def main(args: Array[String]): Unit = {
    //半生对象可以访问类的私有属性
    val s3 = new Student3("aa",12)
    s3.age = 29
    println(s3.age)
    //println(s3.province)不能访问
  }
}