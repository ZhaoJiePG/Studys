package Senior

/**
  * 通俗的讲，比如需要定义一个函数，函数的参数可以接受任意类型。我们不可能一一列举所 有的参数类型重载函数。
  *
  * 那么程序引入了一个称之为泛型的东西，这个类型可以代表任意的数据类型。
  *
  * 例如 List，在创建 List 时，可以传入整形、字符串、浮点数等等任意类型。那是因为 List 在 类定义时引用了泛型
  */
object 泛型枚举 {
  def main(args: Array[String]): Unit = {
    println(ClothesEnum.卫衣)
  }
}

/*
 *scala枚举类型
 */
object ClothesEnum extends Enumeration{
  type ClothesEnum=Value
  val 衣服,裤子,卫衣 =Value
}
/*
 * 定义了一个泛型类
 */
class Massage[T]{
  def getMassage(s:T): Unit ={
    println(s)
  }
}
/*
 * 子类继承指明类型
 */
class strMassage[String] extends Massage{

}
object test extends App{
  private val msg = new Massage[String]
  msg.getMassage("有内鬼，终止交易")
}
