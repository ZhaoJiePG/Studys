package servicenet

import com.alibaba.fastjson.JSONObject
import constants.Constants
import jdbc.JDBCHelperUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import util.DateUtils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by ZJ on 2019-7-6
  * comment:维修员分布
  */
object RepairPersonShow {

  def businessProcess(sparkSession: SparkSession,jdbc:JDBCHelperUtils): Unit ={

    //统计维修工单
    val repairPersonInfo: DataFrame = sparkSession.sql(Constants.REPAIR_PERSON_DESCRIBUTE)
    repairPersonInfo.printSchema()
    repairPersonInfo.show(10)

    //统计全网内维修员的信息
    val repairCount: Long = repairPersonInfo.count()

    val repairAllCountInfoListBuffer: ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val temp = new JSONObject()
    temp.put("type","3")
    temp.put("nation","全国")
    temp.put("province","")
    temp.put("city","twon")
    temp.put("store_level","")
    temp.put("groupby_optition",2)
    temp.put("count",repairCount.toInt)
    temp.put("compute_date",DateUtils.getCurrentDateStr)

    repairAllCountInfoListBuffer += temp
    println("全国维修员数量"+repairAllCountInfoListBuffer.toString())

    // 维修员省范围分租
    val repairProvinceLevelListBuffer: ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val repairGroupByPrivinceDF: DataFrame = repairPersonInfo.groupBy("privince").count()
    repairGroupByPrivinceDF.printSchema()
    repairGroupByPrivinceDF.collect().foreach(row=>{
      val temp = new JSONObject()
      temp.put("type", 3)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("privince"))
      temp.put("city", "")
      temp.put("town", "")
      temp.put("store_level", "")
      temp.put("groupby_option", 1)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      repairProvinceLevelListBuffer += temp
    })
    println("---------------------------维修员省份分布-------------------")
    repairProvinceLevelListBuffer.foreach(row => println(row.toString))

    // 县/区 范围内门店级别分组
    val repairTownLevelListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val repairGroupByTownDF: DataFrame = repairPersonInfo.groupBy("privince", "city", "town").count()
    repairGroupByTownDF.printSchema()
    repairGroupByTownDF.show(10)
    repairGroupByTownDF.collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 3)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("privince"))
      temp.put("city", row.getAs[String]("city"))
      temp.put("town", row.getAs[String]("town"))
      temp.put("store_level", "")
      temp.put("groupby_option", 3)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      repairTownLevelListBuffer += temp
    })
    println("---------------------------维修员县分布-------------------")
    repairTownLevelListBuffer.foreach(row => println(row.toString))

  }
}
