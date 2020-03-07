package servicenet

import com.alibaba.fastjson.JSONObject
import constants.Constants
import jdbc.JDBCHelperUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable

/**
  * Created by ZJ on 2019-7-9
  * comment:售后实时大屏-工单概览[历史部分]
  */
object WorkCardShow {

  def businessProcess(sparkSession: SparkSession, jdbc: JDBCHelperUtils): Unit = {

    val wordCard: DataFrame = sparkSession.read.table("dw.dw_aftersale_repairorder_info_dtl")

    // 近一个月平均响应时长，24小时
    //println("近一个月响应时长均值" + (allResponseCount / allRecordCount))*/

    val workCardListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val groupByHourDataFrame: DataFrame = wordCard.sqlContext.sql(Constants.WORK_CARD_ALL_RECORDS_OF_24_HOUR)
    groupByHourDataFrame.printSchema()
    groupByHourDataFrame.show(10)
    groupByHourDataFrame.collect().map(row=>{
      val temp = new JSONObject()
      temp.put("hour", row.getAs[String]("event_hour"))
      temp.put("hour_count", row.getAs[Long]("hour_count"))
      temp.put("resp_hour_count", row.getAs[Long]("resp_hour_count"))
      temp.put("hourAgg", (row.getAs[Long]("hour_count") / row.getAs[Long]("resp_hour_count")).toInt)
      workCardListBuffer += temp
    })
    println("-------------------------------24小时内平均响应时长--------------------------------------")
    workCardListBuffer.foreach(println)

  }

}

