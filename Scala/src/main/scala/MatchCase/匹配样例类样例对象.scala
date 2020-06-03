package MatchCase

object 匹配样例类样例对象 {
  def main(args: Array[String]): Unit = {
    /*
      * 匹配样例类，样例对象
      */
    def coMatch(ele:Any)=ele match {
      case SubmitTask(id,name)=>println(s"submit task-id:${id},task-name:${name}")
      case CheckTimeOutTask=>println("checking.....")
      case HeartBeat(time)=>println(s"time is ${time}")
    }
    coMatch(SubmitTask("001","node1"))
    coMatch(CheckTimeOutTask)
    coMatch(HeartBeat(8888888L))
    coMatch(HeartBeat(6666))
    // output:
    // submit task-id:001,task-name:node1
    // checking.....
    // time is 8888888
    // time is 6666
  }
}

case object CheckTimeOutTask
case class SubmitTask(id:String,name:String)
case class HeartBeat(time:Long)
