
import org.slf4j.LoggerFactory
/**
  * Created by ZJ on 2021/2/19
  * comment:
  */
object TestLog {


  def main( args : Array[String]) : Unit = {

    val logger = LoggerFactory.getLogger(this.getClass.getName) //注意这个操蛋的东西,如果没对应上,你会遇到Timeout的问题

    System.out.println("----  " + logger.isDebugEnabled)
    for (i <- 0 to 10 ) {
      System.out.println(i)
      logger.warn("----debug message ")
      logger.error("----error message " + i)
      logger.info("----info message " + i)
    }
  }

}
