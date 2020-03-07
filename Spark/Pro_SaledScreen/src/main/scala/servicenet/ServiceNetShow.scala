package servicenet

import com.alibaba.fastjson.{JSONArray, JSONObject}
import jdbc.JDBCHelperUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import util.DateUtils
import util.Redis.cluster.RedisUtils

/**
  * Created by ZJ on 2019/7/12
  * comment:
  */
object ServiceNetShow {
  def businessProcess(sparkSession: SparkSession,jdbc: JDBCHelperUtils): Unit = {
    import sparkSession.implicits._
    import sparkSession.sql

    val storeInfo: DataFrame = sparkSession.read.table("dw.dw_store_info_dtl")
    val storeLevelNationListBuffer:JSONArray = new JSONArray()

    // 删除网点分布上次计算结果
    // jdbc.delStoreDistributeInfoByGroupOption(Constants.DELETE_PRE_STORE_DESTRIBUTE_INFO_SQL,1)
    // 全国范围内门店级别分组

    storeInfo.where("isrepair='service'").groupBy("store_level").count().map(row =>{
      val temp = new JSONObject()
      temp.put("type", 1)
      temp.put("nation", "全国")
      temp.put("privince", "")
      temp.put("city", "")
      temp.put("town", "")
      temp.put("store_level", row.getAs[String]("store_level"))
      temp.put("groupby_option", 0)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      storeLevelNationListBuffer.add(temp)

    })
      // jdbc.executeBatch(storeLevelNationListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)
      RedisUtils.saveNetStoreInfo2Redis(("netStore","nationLevel",storeLevelNationListBuffer))

    /*// 省范围内门店级别分组
    val storeLevelProvinceListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeInfo.where("isrepair = 'service'")
      .groupBy("privince", "store_level").count().collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 1)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("privince"))
      temp.put("city", "")
      temp.put("town", "")
      temp.put("store_level", row.getAs[String]("store_level"))
      temp.put("groupby_option", 1)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      storeLevelProvinceListBuffer += temp
    })
    jdbc.executeBatch(storeLevelProvinceListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)

    // 市范围内门店级别分组
    val storeLevelCityListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeInfo.where("isrepair = 'service'")
      .groupBy("privince", "city", "store_level").count().filter("privince is not null")
      .filter("city is not null").collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 1)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("privince"))
      temp.put("city", row.getAs[String]("city"))
      temp.put("town", "")
      temp.put("store_level", row.getAs[String]("store_level"))
      temp.put("groupby_option", 2)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      storeLevelCityListBuffer += temp
    })
    jdbc.executeBatch(storeLevelCityListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)

    // 县/区 范围内门店级别分组
    val storeLevelTownListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    storeInfo.where("isrepair = 'service'")
      .groupBy("privince", "city", "town", "store_level").count().filter("privince is not null")
      .filter("city is not null").filter("town is not null").collect().foreach(row => {
      val temp = new JSONObject()
      temp.put("type", 1)
      temp.put("nation", "全国")
      temp.put("privince", row.getAs[String]("privince"))
      temp.put("city", row.getAs[String]("city"))
      temp.put("town", row.getAs[String]("town"))
      temp.put("store_level", row.getAs[String]("store_level"))
      temp.put("groupby_option", 3)
      temp.put("count", row.getAs[Int]("count"))
      temp.put("compute_date", DateUtils.getCurrentDateStr)
      storeLevelTownListBuffer += temp
    })
    jdbc.executeBatch(storeLevelTownListBuffer.asJava, Constants.STORE_DESTRIBUTE_INFO_SQL)*/
  }
}
