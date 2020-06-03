package Senior

/**
  * 定义方法时，可以把参数列表标记为 implicit，表示该参数是隐式参数。一个方法只会有一 个隐式参数列表，置于方法的最后一个参数列表。
  * 如果方法有多个隐式参数，只需一个 implicit 修饰即可。
  * 譬如：deffire(x:Int)(implicita:String,b:Int=9527)
  * 当调用包含隐式参数的方法是，如果当前上下文中有合适的隐式值，则编译器会自动为该 组参数填充合适的值，且上下文中只能有一个符合预期的隐式值。
  * 如果没有编译器会抛出 异常。当然，标记为隐式参数的我们也可以手动为该参数添加默认值。
  */
object 隐式参数 {
  def main(args: Array[String]): Unit = {
    def say(implicit content:String): Unit =println(content)
    /*
     * say方法时隐式参数，如果你没有传参数
     * 编译器在编译的时候会自动的从当前上下文中找一个隐式值（符合参数类型的隐式值）
     */
    say("zj")
  }
}
