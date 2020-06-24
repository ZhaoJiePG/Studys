package OOP

object CaseClass extends App{
  val msg = Message("hello")
  println(msg.msgContext)
}

/**
  * 样例类，使用case，主要用于支持模式匹配
  * 样例类默认实现序列化接口,不用使用new
  */
case class Message(msgContext : String)

//样例object，不饿能分装数据，主要用于模式匹配
case object CheckHeartBeat

