package servicenet

import com.alibaba.fastjson.JSONObject
import constants.Constants
import jdbc.JDBCHelperUtils
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import util.{DateUtils, NumberUtils}

import scala.collection.{immutable, mutable}

/**
  * Created by ZJ on 2019-7-9
  * comment:
  */
object QualityShow {
  def businessProcess(sparkSession: SparkSession, jdbc: JDBCHelperUtils): Unit = {
    val prodRepairInfo: Dataset[Row] = sparkSession.read.table("dwd.dwd_prod_repair")
      .filter(row => StringUtils.isNotBlank(row.getAs[String]("sale_time")))
      .filter(row => DateUtils.checkInputIsBetweenYesterdayAndYesteryear(row.getAs[String]("sale_time")))

    //一年内所有的2次维修数量
    val all2RepairCount: Int = prodRepairInfo.rdd.groupBy(_.getAs[String]("vinnumber"))
      .map(row => (row._1, row._2.count(_ => true))).values.reduce(_ + _)
    println("一年内所有的2次维修次数" + all2RepairCount)

    val fittingErrorTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    // 删除销售上次计算结果
    //jdbc.delStoreDistributeInfoByGroupOption(Constants.DELETE_PRE_STORE_DESTRIBUTE_INFO_SQL, 2)
    // top10配件故障率
    import sparkSession.implicits._
    val prodRepairCount: Long = prodRepairInfo.count()
    prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_TOP_10_FITTING).collect()
      .foreach(row => {
        val temp = new JSONObject()
        temp.put("repairment_name", row.getAs[String]("repairment_name"))
        temp.put("allFittingCount", prodRepairCount)
        temp.put("itemFittingCount", row.getAs[Int]("repair_item_count"))
        temp.put("itemFittingRepairRate", NumberUtils.get2NumPercentage(row.getAs[Long]("repair_item_count"), prodRepairCount))
        fittingErrorTop10ListBuffer += temp
      })
    println("-------------------------------top10配件故障率  --------------------------------------")
    fittingErrorTop10ListBuffer.foreach(println)

    // top10省的二次返修率
    val provinceErrorTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_TOP_10_PROVINCE).collect()
      .foreach(row => {
        val temp = new JSONObject()
        temp.put("province", row.getAs[String]("repair_province"))
        temp.put("all2RepairCount", all2RepairCount)
        temp.put("provinceRepairCount", row.getAs[Int]("repair_sum"))
        temp.put("provinceItemFittingRepairRate", NumberUtils.get2NumPercentage(row.getAs[Long]("repair_sum"), all2RepairCount))
        provinceErrorTop10ListBuffer += temp
      })
    println("-------------------------------top10省的二次返修率  1 --------------------------------------")
    provinceErrorTop10ListBuffer.foreach(println)

    provinceErrorTop10ListBuffer.clear()

    // top10车型二次返修率(维度:车型->返修率
    val carModeTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_PROVINCE_TOP_10_CARMODE_EXCLUDE).rdd
      .groupBy(row => row.getAs[String]("ecc_name"))
      .map(carModeIter => {
        val carModeName: String = carModeIter._1
        val carModeSum: Long = carModeIter._2.map(_.getAs[Long]("repair_count")).sum
        (all2RepairCount, carModeName, carModeSum)
      }).collect().sortWith(_._3 > _._3).take(10)
      .foreach(x => {
        val temp = new JSONObject()
        temp.put("allRepair2Count", x._1)
        temp.put("carModeName", x._2)
        temp.put("repair2CountForEveryCarMode", x._3)
        temp.put("carModeItemRepairRate", NumberUtils.get2NumPercentage(x._3, x._1))
        carModeTop10ListBuffer += temp
      })
    println("-------------------------------top10车型二次返修率 不包含省维度--------------------------------------")
    carModeTop10ListBuffer.foreach(println)
    carModeTop10ListBuffer.clear()

    val resTuple: Array[List[(String, Long, String, Long)]] = prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_PROVINCE_TOP_10_CARMODE_INCLUDE).rdd
      .groupBy(_.getAs[String]("repair_province")).map(procinveIter=> {
      val provinceName: String = procinveIter._1
      val provinceRepairCount: Long = procinveIter._2.map(_.getAs[Long]("repair_sum")).sum
      val resTuple: immutable.Iterable[(String, Long, String, Long)] = procinveIter._2.groupBy(_.getAs[String]("ecc_name")).map(eccIter => {
        val eccName: String = eccIter._1
        val eccCount: Long = eccIter._2.map(_.getAs[Long]("repair_count")).sum
        (provinceName, provinceRepairCount, eccName, eccCount)
      })
      resTuple.toList.sortWith(_._4>_._4).take(10)
    }).collect()
    for (elem1 <- resTuple) {
      for (elem2 <- elem1) {
        val temp = new JSONObject()
        temp.put("provinceName", elem2._1)
        temp.put("repairCountOfEveryProvince", elem2._2)
        temp.put("carMode", elem2._3)
        temp.put("carModeCount", elem2._4)
        temp.put("carModeItemFittingRepairRate", NumberUtils.get2NumPercentage(elem2._4, elem2._2))
        carModeTop10ListBuffer += temp
      }
    }
    println("-------------------------------top10车型二次返修率 包含省维度 new --------------------------------------")
    carModeTop10ListBuffer.foreach(println)
    carModeTop10ListBuffer.clear()

    //  ****************************  sparkcore备用实现用   start   ***************************
    /*// top10车型二次返修率(维度:省->车型->返修率
    val tuples: Array[(String, String, Long)] = prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_PROVINCE_TOP_10_CARMODE_INCLUDE).collect()
      .map(row => {
        (row.getAs[String]("repair_province"), row.getAs[String]("ecc_name"),
          row.getAs[Long]("repair_count"))
      })
    // [(省,车型,二次返修次数)]]
    val groupByProvince: immutable.Iterable[(String, String, Long)] = tuples.groupBy(_._1).flatMap(_._2).map(row => (row._1, row._2, row._3))
    val rddGroupByProvince: RDD[(String, String, Long)] = sparkSession.sparkContext.parallelize(groupByProvince.toSeq)
    val rddGroupByProvince2: RDD[(String, Iterable[(String, String, Long)])] = rddGroupByProvince.groupBy(_._1)
    // RDD[(省, 省下全部的2次维修数量, List[(车型, 各车型所有数量)])]
    val unit: RDD[(String, Long, List[(String, Long)])] = rddGroupByProvince2.map(row => {
      val provinceName: String = row._1
      val repairCountOfEveryProvince: Long = row._2.map(_._3).sum
      // 每个省内根据车型分组 => (车型, Iterable[(String, String, Long)]) => map(将每个省内同车型数量累加) => map(车型,各车型迭代器count) => 排序 取top 10
      val list: List[(String, Long)] = row._2.groupBy(_._2)
        .map(r => (r._1, r._2.reduce((a, b) => (a._1, a._2, a._3 + b._3))))
        .map(row => (row._1, row._2._3)).toList.sortWith(_._2 > _._2).take(10)
      (provinceName, repairCountOfEveryProvince, list)
    })
    val array: Array[List[(String, Long, String, Long)]] = unit.collect().map(k => k._3.map(f => (k._1, k._2, f._1, f._2)))
    for (elem1 <- array) {
      for (elem2 <- elem1) {
        val temp = new JSONObject()
        temp.put("provinceName", elem2._1)
        temp.put("repairCountOfEveryProvince", elem2._2)
        temp.put("carMode", elem2._3)
        temp.put("carModeCount", elem2._4)
        temp.put("carModeItemFittingRepairRate", NumberUtils.get2NumPercentage(elem2._4, elem2._2))
        carModeTop10ListBuffer += temp
      }
    }
    println("-------------------------------top10车型二次返修率 old--------------------------------------")
    carModeTop10ListBuffer.foreach(println)*/
    //  ****************************  sparkcore备用实现用   end   ***************************

    // 分基地（故障率）
    val basicErrorTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val countByFilterBasicName: Long = prodRepairInfo.where($"basic_name".isNotNull).count()
    prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_GROUP_BY_BASIC).collect()
      .foreach(row => {
        val temp = new JSONObject()
        temp.put("basicCode", row.getAs[String]("basic_code"))
        temp.put("basicName", row.getAs[String]("basic_name"))
        temp.put("basicCount", row.getAs[Int]("basic_count"))
        temp.put("allCount", countByFilterBasicName)
        temp.put("errorRateForBasicName", NumberUtils.get2NumPercentage(row.getAs[Long]("basic_count"), countByFilterBasicName))
        basicErrorTop10ListBuffer += temp
      })
    println("-------------------------------分基地（故障率）--------------------------------------")
    basicErrorTop10ListBuffer.foreach(println)


    // 各基地下top10车型（故障率）
    val basicCarModeErrorTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val tupleRDD2: RDD[(String, String, Long)] = prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_GROUP_BY_BASIC_CAR_MODE).rdd
      .map(row => (row.getAs[String]("basic_code") + "_" + row.getAs[String]("basic_name"),
        row.getAs[String]("ecc_name"), row.getAs[Long]("ecc_name_count")))
    val basicCarModeTop10Error: Array[List[(String, Long, String, Long)]] = tupleRDD2.groupBy(_._1).map(basicIter => {
      // 各基地维修数
      val basicNameAndCode: String = basicIter._1
      val basicRepairCount: Long = basicIter._2.map(_._3).sum
      // 每个基地内车型分组
      val groupByEccName: List[(String, Iterable[(String, String, Long)])] = basicIter._2.groupBy(_._2).toList
      groupByEccName.map(row => {
        (row._1, row._2.map(_._3).sum)
      }).sortWith(_._2 > _._2).take(10).map(row => (basicNameAndCode, basicRepairCount, row._1, row._2))
    }).collect()
    for (elem <- basicCarModeTop10Error) {
      for (elem2 <- elem) {
        val temp = new JSONObject()
        temp.put("basicCode", elem2._1.split("_")(0))
        temp.put("basicName", if (elem2._1.split("_").length > 1) elem2._1.split("_")(1) else "")
        temp.put("basicErrorAllCount", elem2._2)
        temp.put("carModeName", elem2._3)
        temp.put("carModeCount", elem2._4)
        temp.put("basicCarModeErrorRateForBasicName", NumberUtils.get2NumPercentage(elem2._4, elem2._2))
        basicCarModeErrorTop10ListBuffer += temp
      }
    }
    println("-------------------------------各基地下top10车型（故障率）--------------------------------------")
    basicCarModeErrorTop10ListBuffer.foreach(println)



    // 各基地下各车型下top10的SABC
    val basicCarModeSABCErrorTop10ListBuffer: mutable.ListBuffer[JSONObject] = scala.collection.mutable.ListBuffer[JSONObject]()
    val tupleRDD: RDD[(String, String, String, Long)] = prodRepairInfo.sqlContext.sql(Constants.YESTERYEAR_ERROR_GROUP_BY_BASIC_CAR_AND_SABC).rdd
      .map(row => (row.getAs[String]("basic_code") + "_" + row.getAs[String]("basic_name"),
        row.getAs[String]("ecc_name"), row.getAs[String]("prod_level"), row.getAs[Long]("prod_level_count")))

    val resTupleIN: RDD[List[immutable.Iterable[(String, Long, String, Long, Long, String, Long)]]] = tupleRDD.groupBy(_._1).map(basicIter => {
      val basicNameAndCode: String = basicIter._1
      // 各基地名称_编码
      val basicRepairCount: Long = basicIter._2.map(_._4).sum // 各基地维修总数
      // 每个基地内车型分组
      val groupByEccName: List[(String, Iterable[(String, String, String, Long)])] = basicIter._2.groupBy(_._2).toList
      val carModeCountTop10: List[(String, Long, String, Long, Iterable[(String, String, String, Long)])] = groupByEccName.map(row => {
        val carMode: String = row._1 // 车型名称
        val carModeCount: Long = row._2.map(_._4).sum // 每个基地下每个车型维修数量
        (basicNameAndCode, basicRepairCount, carMode, carModeCount, row._2)
      }).sortWith(_._4 > _._4).take(10)
      // 按照SABC分组
      val resTupleIN: List[immutable.Iterable[(String, Long, String, Long, Long, String, Long)]] = carModeCountTop10.map(row => {
        val basicNameAndCode: String = row._1
        val basicRepairCount: Long = row._2
        val carMode: String = row._3
        val carModeCount: Long = row._4
        val sabcAllCount: Long = row._5.map(_._4).sum
        val resTupleIN: immutable.Iterable[(String, Long, String, Long, Long, String, Long)] = row._5.groupBy(_._3).map(x => {
          val sabcType: String = x._1
          val sabcCount: Long = x._2.map(_._4).sum
          (basicNameAndCode, basicRepairCount, carMode, carModeCount, sabcAllCount, sabcType, sabcCount)
        })
        resTupleIN
      })
      resTupleIN
    })
    for (elem <- resTupleIN.collect()) {
      for (elem2 <- elem) {
        for (elem3 <- elem2) {
          val temp = new JSONObject()
          temp.put("basicCode", elem3._1.split("_")(0))
          temp.put("basicName", if (elem3._1.split("_").length > 1) elem3._1.split("_")(1) else "")
          temp.put("basicErrorAllCount", elem3._2)
          temp.put("carModeName", elem3._3)
          temp.put("carModeCount", elem3._4)
          temp.put("sabcAllCount", elem3._5)
          temp.put("sabcType", elem3._6)
          temp.put("sabcCount", elem3._7)
          temp.put("basicCarModeErrorRateForBasicName", NumberUtils.get2NumPercentage(elem3._7, elem3._5))
          basicCarModeSABCErrorTop10ListBuffer += temp
        }
      }
    }
    println("-------------------------------各基地下各车型top10下sabc故障率占比--------------------------------------")
    basicCarModeSABCErrorTop10ListBuffer.foreach(println)
  }
}
