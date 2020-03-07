import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 创建一个执行环境，表示当前执行程序的上下文。 如果程序是独立调用的，则
 * 此方法返回本地执行环境
 */
object Environment {
  def main(args: Array[String]): Unit = {
    //getExecutionEnvironment 会根据查询运行的方
    //式决定返回什么样的运行环境，是最常用的一种创建执行环境的方式
    val env1: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 返回流式环境
    val env2: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //返回本地执行环境，需要在调用时指定默认的并行度。
    val env3: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1)

    // 返回集群执行环境，将 Jar 提交到远程服务器。需要在调用时指定 JobManager的 IP 和端口号，并指定要在集群中运行的 Jar 包
    val env4: ExecutionEnvironment = ExecutionEnvironment.createRemoteEnvironment("127.0.0.1",6123,"./.jar")
  }
}
