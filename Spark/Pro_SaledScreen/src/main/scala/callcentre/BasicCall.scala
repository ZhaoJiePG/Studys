package callcentre

import jdbc.JDBCHelperUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, RelationalGroupedDataset, Row, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010._
import util.{DateUtils, NumberUtils}
import util.SparkTools.SparkUtils

import scala.collection.mutable

/**
  * 呼叫中心业务处理
  */
object BasicCall extends Serializable {

  def businessProcess(sparkSession: SparkSession, jdbc: JDBCHelperUtils): Unit = {
    import sparkSession.implicits._
    import sparkSession.sql

    val save2DB: Array[Any] = new Array[Any](62)
    val options: Map[String, String] = SparkUtils.getSparkSQLForJDBConfigOptions()

    val billsDataFrame: DataFrame = sparkSession.read.format("jdbc").options(options)
      .option("dbtable", "dbo.CC_BILLS").load()
    //.persist(StorageLevel.MEMORY_ONLY_SER)

    // 当日投诉量
    val curr_day_complaint_count: Long = billsDataFrame.where("billtype = 1003 ")
      .where("regTime >= '" + DateUtils.getCurrentDateStratDateTimeStr + "'").
      where("regTime <= '" + DateUtils.getCurrentDateEndDateTimeStr + "'").count()
    println("当日投诉量" + curr_day_complaint_count)
    save2DB(0) = curr_day_complaint_count

    // 工作时间接通率：8点-17点  接通数/转坐席总数
    val cologDataFrame: DataFrame = sparkSession.read.format("jdbc").options(options)
      .option("dbtable", "dbo.COLOG").load()
    //.persist(StorageLevel.MEMORY_ONLY_SER)
    // 8点-17点  接通数
    val countA8_17: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrDayDateTimeStrByHour_Min("08")
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Min("17") + "'")
      .where("CallType=0 And TransResult != 0").dropDuplicates("COID").count()
    println(" 8点-17点  接通数" + countA8_17)

    // 8点-17点  转坐席总数
    val countB8_17: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrDayDateTimeStrByHour_Min("08")
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Min("17") + "'")
      .where(" TimeLength < 5000 And TransResult != 0").count()
    println(" 8点-17点  转坐席总数" + countB8_17)
    println(" 工作时间接通率：8点-17点  接通数/转坐席总数 : " + NumberUtils.get2NumPercentage(countA8_17, countB8_17))
    save2DB(1) = NumberUtils.get2NumPercentage(countB8_17, countA8_17)

    // 接通率：24小时
    // 24 小时 接通数
    val countA: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrentDateStratDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrentDateEndDateTimeStr + "'")
      .where(" CallType=0 And TransResult != 0").dropDuplicates("COID").count()
    println(" 当天所有接通数" + countA)

    // 24 小时  转坐席总数
    val countB: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrentDateStratDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrentDateEndDateTimeStr + "'")
      .where(" TimeLength < 5000 And TransResult != 0").count()
    println(" 24 小时  转坐席总数" + countB)
    println("当日24小时内接通率:  接通数/转坐席总数 : " + NumberUtils.get2NumPercentage(countA, countB))
    save2DB(2) = NumberUtils.get2NumPercentage(countB, countA)


    // 当月投诉量
    val curr_month_complaint_count: Long = billsDataFrame.where("billtype = 1003 ")
      .where("regTime >= '" + DateUtils.getCurrMonthFirstDayStartDateTimeStr + "'").
      where("regTime <= '" + DateUtils.getCurrentDateTimeStr + "'").count()
    println("当月投诉量" + curr_month_complaint_count)
    save2DB(3) = curr_month_complaint_count

    // 当月拉通率  =  当月拉通率
    // 当月接通数
    val countAMonth: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrMonthFirstDayStartDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrentDateTimeStr + "'")
      .where(" CallType=0 And TransResult != 0").dropDuplicates("COID").count()
    println(" 当月所有接通数" + countAMonth)

    // 当月 转坐席总数
    val countBMonth: Long = cologDataFrame.where("createTime>='" + DateUtils.getCurrMonthFirstDayStartDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrentDateTimeStr + "'")
      .where(" TimeLength < 5000 And TransResult != 0").count()
    // 当月 转坐席总数
    println(" 当月 转坐席总数" + countBMonth)
    // 当月接通率
    println("当月接通率 : " + NumberUtils.get2NumPercentage(countBMonth, countAMonth))
    save2DB(4) = NumberUtils.get2NumPercentage(countBMonth, countAMonth)


    // 来电量（总）
    val allCallIn: Long = cologDataFrame.where("CallType = 0 and CallResult=1").dropDuplicates("COID").count()
    println("来电量（总） : " + allCallIn)
    save2DB(5) = allCallIn

    // 转入量（系统打进来的量）
    val switchToCount: Long = cologDataFrame.where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
    println("转入量（系统打进来的量） : " + switchToCount)
    save2DB(6) = switchToCount


    // 接听量：*  = 接听量 = 转坐席成功量
    val answerCount: Long = cologDataFrame.where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
    println("接听量：*  = 接听量 = 转坐席成功量 : " + answerCount)
    save2DB(7) = answerCount

    //当日24h来电趋势图（转入量、接听量）  1点钟数据为1:00:00 到 1:59:59    24点数据为前一天:00:00:00 到00:59:59秒
    val switchToHour1Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("01")
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("01") + "'")
      .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
    println("转入量（系统打进来的量）1点 : " + switchToHour1Count)
    save2DB(8) = switchToHour1Count
    val answerHour1Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("01")
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("01") + "'")
      .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
    println("接听量  1点: " + answerHour1Count)
    save2DB(9) = answerHour1Count


    if (DateUtils.getCureentHour >= 2) {
      val switchToHour2Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("02")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("02") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）2点 : " + switchToHour2Count)
      save2DB(10) = switchToHour2Count
      val answerHour2Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("02")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("02") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量   2点 : " + answerHour2Count)
      save2DB(11) = answerHour2Count
    } else {
      save2DB(10) = 0
      save2DB(11) = 0
    }

    if (DateUtils.getCureentHour >= 3) {
      val switchToHour3Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("03")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("03") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）3点 : " + switchToHour3Count)
      save2DB(12) = switchToHour3Count
      val answerHour3Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("03")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("03") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量  3点: " + answerHour3Count)
      save2DB(13) = answerHour3Count
    } else {
      save2DB(12) = 0
      save2DB(13) = 0
    }

    if (DateUtils.getCureentHour >= 4) {
      val switchToHour4Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("04")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("04") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）4点 : " + switchToHour4Count)
      save2DB(14) = switchToHour4Count
      val answerHour4Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("04")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("04") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 4点" + answerHour4Count)
      save2DB(15) = answerHour4Count
    } else {
      save2DB(14) = 0
      save2DB(15) = 0
    }

    if (DateUtils.getCureentHour >= 5) {
      val switchToHour5Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("05")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("05") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）5点 : " + switchToHour5Count)
      save2DB(16) = switchToHour5Count
      val answerHour5Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("05")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("05") + "'")
        .where("TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 5点" + answerHour5Count)
      save2DB(17) = answerHour5Count
    } else {
      save2DB(16) = 0
      save2DB(17) = 0
    }

    if (DateUtils.getCureentHour >= 6) {
      val switchToHour6Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("06")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("06") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）6点 : " + switchToHour6Count)
      save2DB(18) = switchToHour6Count
      val answerHour6Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("06")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("06") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 6点" + answerHour6Count)
      save2DB(19) = answerHour6Count
    } else {
      save2DB(18) = 0
      save2DB(19) = 0
    }

    if (DateUtils.getCureentHour >= 7) {
      val switchToHour7Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("07")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("07") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）7点 : " + switchToHour7Count)
      save2DB(20) = switchToHour7Count
      val answerHour7Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("07")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("07") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 7点" + answerHour7Count)
      save2DB(21) = answerHour7Count
    } else {
      save2DB(20) = 0
      save2DB(21) = 0
    }

    if (DateUtils.getCureentHour >= 8) {
      val switchToHour8Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("08")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("08") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）8点 : " + switchToHour8Count)
      save2DB(22) = switchToHour8Count
      val answerHour8Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("08")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("08") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 8点" + answerHour8Count)
      save2DB(23) = answerHour8Count
    } else {
      save2DB(22) = 0
      save2DB(23) = 0
    }

    if (DateUtils.getCureentHour >= 9) {
      val switchToHour9Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("09")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("09") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）9点 : " + switchToHour9Count)
      save2DB(24) = switchToHour9Count
      val answerHour9Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("09")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("09") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 9点" + answerHour9Count)
      save2DB(25) = answerHour9Count
    } else {
      save2DB(24) = 0
      save2DB(25) = 0
    }


    if (DateUtils.getCureentHour >= 10) {
      val switchToHour10Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("10")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("10") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）10点 : " + switchToHour10Count)
      save2DB(26) = switchToHour10Count
      val answerHour10Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("10")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("10") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 10点" + answerHour10Count)
      save2DB(27) = answerHour10Count
    } else {
      save2DB(26) = 0
      save2DB(27) = 0
    }


    if (DateUtils.getCureentHour >= 11) {
      val switchToHour11Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("11")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("11") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）11点 : " + switchToHour11Count)
      save2DB(28) = switchToHour11Count
      val answerHour11Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("11")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("11") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 11点" + answerHour11Count)
      save2DB(29) = answerHour11Count
    } else {
      save2DB(28) = 0
      save2DB(29) = 0
    }


    if (DateUtils.getCureentHour >= 12) {
      val switchToHour12Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("12")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("12") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）12点 : " + switchToHour12Count)
      save2DB(30) = switchToHour12Count
      val answerHour12Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("12")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("12") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 12点" + answerHour12Count)
      save2DB(31) = answerHour12Count
    } else {
      save2DB(30) = 0
      save2DB(31) = 0
    }


    if (DateUtils.getCureentHour >= 13) {
      val switchToHour13Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("13")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("13") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）13点 : " + switchToHour13Count)
      save2DB(32) = switchToHour13Count
      val answerHour13Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("13")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("13") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 13点" + answerHour13Count)
      save2DB(33) = answerHour13Count
    } else {
      save2DB(32) = 0
      save2DB(33) = 0
    }


    if (DateUtils.getCureentHour >= 14) {
      val switchToHour14Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("14")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("14") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）14点 : " + switchToHour14Count)
      save2DB(34) = switchToHour14Count
      val answerHour14Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("14")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("14") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 14点" + answerHour14Count)
      save2DB(35) = answerHour14Count
    } else {
      save2DB(34) = 0
      save2DB(35) = 0
    }


    if (DateUtils.getCureentHour >= 15) {
      val switchToHour15Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("15")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("15") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）15点 : " + switchToHour15Count)
      save2DB(36) = switchToHour15Count
      val answerHour15Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("15")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("15") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 15点" + answerHour15Count)
      save2DB(37) = answerHour15Count
    } else {
      save2DB(36) = 0
      save2DB(37) = 0
    }


    if (DateUtils.getCureentHour >= 16) {
      val switchToHour16Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("16")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("16") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）16点 : " + switchToHour16Count)
      save2DB(38) = switchToHour16Count
      val answerHour16Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("16")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("16") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 16点" + answerHour16Count)
      save2DB(39) = answerHour16Count
    } else {
      save2DB(38) = 0
      save2DB(39) = 0
    }



    if (DateUtils.getCureentHour >= 17) {
      val switchToHour17Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("17")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("17") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）17点 : " + switchToHour17Count)
      save2DB(40) = switchToHour17Count
      val answerHour17Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("17")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("17") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 17点" + answerHour17Count)
      save2DB(41) = answerHour17Count
    } else {
      save2DB(40) = 0
      save2DB(41) = 0
    }


    if (DateUtils.getCureentHour >= 18) {
      val switchToHour18Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("18")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("18") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）18点 : " + switchToHour18Count)
      save2DB(42) = switchToHour18Count
      val answerHour18Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("18")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("18") + "'")
        .where("TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 18点" + answerHour18Count)
      save2DB(43) = answerHour18Count
    } else {
      save2DB(42) = 0
      save2DB(43) = 0
    }


    if (DateUtils.getCureentHour >= 19) {
      val switchToHour19Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("19")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("19") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）19点 : " + switchToHour19Count)
      save2DB(44) = switchToHour19Count
      val answerHour19Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("19")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("19") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 19点" + answerHour19Count)
      save2DB(45) = answerHour19Count
    } else {
      save2DB(44) = 0
      save2DB(45) = 0
    }

    if (DateUtils.getCureentHour >= 20) {
      val switchToHour20Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("20")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("20") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）20点 : " + switchToHour20Count)
      save2DB(46) = switchToHour20Count
      val answerHour20Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("20")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("20") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 20点" + answerHour20Count)
      save2DB(47) = answerHour20Count
    } else {
      save2DB(46) = 0
      save2DB(47) = 0
    }


    if (DateUtils.getCureentHour >= 21) {
      val switchToHour21Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("21")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("21") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）21点 : " + switchToHour21Count)
      save2DB(48) = switchToHour21Count
      val answerHour21Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("21")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("21") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 21点" + answerHour21Count)
      save2DB(49) = answerHour21Count
    } else {
      save2DB(48) = 0
      save2DB(49) = 0
    }


    if (DateUtils.getCureentHour >= 22) {
      val switchToHour22Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("22")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("22") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）22点 : " + switchToHour22Count)
      save2DB(50) = switchToHour22Count
      val answerHour22Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("22")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("22") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 22点" + answerHour22Count)
      save2DB(51) = answerHour22Count
    } else {
      save2DB(50) = 0
      save2DB(51) = 0
    }


    if (DateUtils.getCureentHour >= 23) {
      val switchToHour23Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("23")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("23") + "'")
        .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
      println("转入量（系统打进来的量）23点 : " + switchToHour23Count)
      save2DB(52) = switchToHour23Count
      val answerHour23Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getCurrDayDateTimeStrByHour_Min("23")
        + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("23") + "'")
        .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
      println("接听量 : 23点" + answerHour23Count)
      save2DB(53) = answerHour23Count
    } else {
      save2DB(52) = 0
      save2DB(53) = 0
    }

    // 当天0点数据-->前一天23:59:59  到 00:59:59
    val switchToHour24Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getPreDayEndDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("00") + "'")
      .where("CallType = 0 And TransResult != 0").dropDuplicates("COID").count()
    println("转入量（系统打进来的量）24点 : " + switchToHour24Count)
    save2DB(54) = switchToHour24Count
    val answerHour24Count: Long = cologDataFrame.where("createTime>'" + DateUtils.getPreDayEndDateTimeStr
      + "'  and createTime<='" + DateUtils.getCurrDayDateTimeStrByHour_Max("00") + "'")
      .where(" TimeLength <5000 And TransResult = 1").dropDuplicates("COID").count()
    println("接听量 : 24点" + answerHour24Count)
    save2DB(55) = answerHour24Count

    // 来电分类：咨询量、售后量、投诉量、加盟量
    val tellTypes: collection.Map[Int, Long] = billsDataFrame.rdd.repartition(4).map(h => {
      (h.getAs[Int]("BillType"), 1)
    }).countByKey()
    println("来电分类：咨询量、售后量、投诉量、加盟量 :" + tellTypes.toString())
    save2DB(56) = tellTypes.getOrElse(1001, 0)
    save2DB(57) = tellTypes.getOrElse(1002, 0)
    save2DB(58) = tellTypes.getOrElse(1003, 0)
    save2DB(59) = tellTypes.getOrElse(1005, 0)

    // 受理人数：* 只接入电话的工作人员数量
    val agentworkmodelogDataFrame: DataFrame = sparkSession.read.format("jdbc").options(options)
      .option("dbtable", "dbo.AGENTWORKMODELOG").load()
    val dateCompare: (String, String) => Boolean = (input1: String, input2: String) => DateUtils.get2DateTimeCompare(input1.substring(0, input1.indexOf(".")), input2.substring(0, input2.indexOf(".")))
    val intToLong: collection.Map[Int, Long] = agentworkmodelogDataFrame.where("StartTime>'" + DateUtils.getCurrentDateStratDateTimeStr
      + "'  and StartTime<='" + DateUtils.getCurrentDateEndDateTimeStr + "'").rdd.groupBy(row => row.getAs[String]("EmployeeID"))
      .map(a => (a._1, a._2.toList.sortWith((b, c) => dateCompare(b.getTimestamp(4).toString, c.getTimestamp(4).toString))
        .take(1))).map(x => (x._2.head.getAs[Int]("WorkMode"), 1)).countByKey()
    val acceptCount: Long = intToLong.getOrElse(-1, 0)
    println("受理人数：* 只接入电话的工作人员数量" + acceptCount)
    save2DB(60) = acceptCount
    val processCount: Long = intToLong.getOrElse(3, 0)
    println("处理人数：*只外拨电话的:" + processCount)
    save2DB(61) = processCount
    jdbc.updateBasicCallInfoById(save2DB.asInstanceOf[Array[AnyRef]])

  }
}
