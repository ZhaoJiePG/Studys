package servicenet

import com.alibaba.fastjson.JSONObject
import constants.Constants
import jdbc.JDBCHelperUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import util.DateUtils

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * 售后实时大屏-全国省市县级别销售热力对比
  */
object StoreSaleShow {

  def businessProcess(sparkSession: SparkSession, jdbc: JDBCHelperUtils): Unit = {
    val storeSaleInfo: DataFrame = sparkSession.read.table("dwd.dwd_sale_wide")
    val saleNationLevelListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    // 删除销售上次计算结果
    jdbc.delStoreDistributeInfoByGroupOption(Constants.DELETE_PRE_STORE_DESTRIBUTE_INFO_SQL, 2)
    // 全国销售
    val nationSale = new JSONObject()
    nationSale.put("type", 2)
    nationSale.put("nation", "全国")
    nationSale.put("privince", "")
    nationSale.put("city", "")
    nationSale.put("town", "")
    nationSale.put("store_level", "")
    nationSale.put("groupby_option", 0)
    nationSale.put("count", storeSaleInfo.count())
    nationSale.put("compute_date", DateUtils.getCurrentDateStr)
    saleNationLevelListBuffer += nationSale
    jdbc.executeBatch(saleNationLevelListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)

    // 省范围内门店级别分组
    val saleProvinceLevelListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeSaleInfo.groupBy("store_province").count().filter("store_province is not null").collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 2)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("store_province"))
      temp.put("city", "")
      temp.put("town", "")
      temp.put("store_level", "")
      temp.put("groupby_option", 1)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      saleProvinceLevelListBuffer += temp
    })
    jdbc.executeBatch(saleProvinceLevelListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)

    // 市范围内门店级别分组
    val saleCityLevelListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeSaleInfo.groupBy("store_province", "store_city").count().filter("store_province is not null")
      .filter("store_city is not null").collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 2)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("store_province"))
      temp.put("city", row.getAs[String]("store_city"))
      temp.put("town", "")
      temp.put("store_level", "")
      temp.put("groupby_option", 2)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      saleCityLevelListBuffer += temp
    })
    jdbc.executeBatch(saleCityLevelListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)

    // 县/区 范围内门店级别分组
    val saleAreaLevelListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeSaleInfo.groupBy("store_province", "store_city", "store_area").count().filter("store_province is not null")
      .filter("store_city is not null").filter("store_area is not null").collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 2)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("store_province"))
      temp.put("city", row.getAs[String]("store_city"))
      temp.put("town", row.getAs[String]("store_area"))
      temp.put("store_level", "")
      temp.put("groupby_option", 3)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      saleAreaLevelListBuffer += temp
    })
    jdbc.executeBatch(saleAreaLevelListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)
  }
}