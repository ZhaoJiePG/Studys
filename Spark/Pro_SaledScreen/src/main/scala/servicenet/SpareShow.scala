package servicenet

import constants.Constants
import jdbc.JDBCHelperUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import util.{DateUtils, NumberUtils}

/**
  * Created by ZJ on 2019/7/12
  * comment:配件模块
  */
object SpareShow {
  def businessProcess(sparkSession: SparkSession, jdbc: JDBCHelperUtils): Unit = {
    import sparkSession.implicits._
    import sparkSession.sql
    val spareDF: DataFrame = sparkSession.sql(Constants.SPARE_PART_SQL)
    // 交货及时率
    // 总条数
    val recordCount: Long = spareDF.count()
    // 订单行发货及时数
    val percentRecordCount: Long = spareDF.rdd.filter(row => {
      if (DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
        row.getAs[String]("invoice_header_wadat_ist")) > 0) false else true
    }).count()
    // 交货及时率
    val promptness: String = NumberUtils.get2NumPercentage(percentRecordCount, recordCount)

    val unissuedDF: Dataset[Row] = spareDF.filter(" vbup_wbsta != 'C' ")

    // 未发配件数量/分区域
    val unissuedGroupByArea: DataFrame = unissuedDF.filter("order_head_province is not null and order_head_province != '' ")
      .groupBy("order_head_province").sum("order_line_kwmeng")

    // 未发配件数量/分基地
    val unissuedGroupByBasic: DataFrame = unissuedDF.groupBy("t188t_vtext").sum("order_line_kwmeng")
    // 未发配件数量/分时间/超4天
    val unissuedUp4Day: Long = unissuedDF.filter(row => {
      if (DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
        DateUtils.getCurrentDateStr) > 4) true else false
    }).map(_.getAs[String]("order_line_kwmeng").toLong).reduce(_ + _)

    // 未发配件数量/分时间/超10天
    val unissuedUp10Day: Long = unissuedDF.filter(row => {
      if (DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
        DateUtils.getCurrentDateStr) > 10) true else false
    }).map(_.getAs[String]("order_line_kwmeng").toLong).reduce(_ + _)
    // 未发配件数量/分时间/超30天
    val unissuedUp30Day: Long = unissuedDF.filter(row => {
      if (DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
        DateUtils.getCurrentDateStr) > 30) true else false
    }).map(_.getAs[String]("order_line_kwmeng").toLong).reduce(_ + _)

    // 近12个月所有发货信息
    val invoic12month: Dataset[Row] = spareDF.filter(row => DateUtils.checkInputInYester12month(row.getAs[String]("invoice_header_wadat_ist")))
      .filter(" invoice_header_wadat_ist is not null and invoice_header_wadat_ist != '' and vbup_wbsta = 'C' ")

    // 平均发货时长,分基地
//    val basicInvoiceAgg: RDD[(String, Int, Long)] = invoic12month.map(row => {
//      val basic_factory: String = row.getAs[String]("t188t_vtext")
//      val diffDays: Long = DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
//        DateUtils.getCurrentDateStr)
//      (basic_factory, diffDays)
//    }).rdd.groupBy(_._1).map(t => {
//      (t._1, t._2.size, t._2.reduce(_._2 + _._2))
//    })

    // 12个月发货时间趋势
//    val groupByMonth: RDD[(String, Iterable[Row])] = invoic12month.rdd.groupBy(row => row.getAs[String]("invoice_header_wadat_ist").substring(5, 7))
//    val month12Ivoice : RDD[(String, Int, Long)] = groupByMonth.map(row => {
//      val month: String = row._1
//      val monthIterSize: Int = row._2.size
//      val invoiceCastDays: Long = row._2.map(row => DateUtils.get2DateDiffDay(row.getAs[String]("order_head_vdatu"),
//        DateUtils.getCurrentDateStr)).sum
//      (month, monthIterSize, invoiceCastDays)
//    })
  }
}
