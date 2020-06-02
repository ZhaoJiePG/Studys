package Basic

object 变量的定义 {
  def main(args: Array[String]): Unit = {
    /**
      * 定义变量使用var或者val关 键 字
      *
      * 语法:
      *  var | val 变量名称(: 数据类型) =变量值
      */
    // 使用val修饰的变量, 值不能为修改,相当于java中final修饰的变量
    val name = "tom"

    // 使用var修饰的变量,值可以修改
    var age = 18

    // 定义变量时,可以指定数据类型,也可以不指定,不指定时编译器会自动推测变量的数据类型
    val name2 : String = "jack"
  }
}
