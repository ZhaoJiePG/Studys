package com.yadea.util

import java.time._
import java.time.format.DateTimeFormatter

object DateScalaUtils {

  private val formatter_ymd: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  //字符串时间戳格式化yyyy-MM-dd
  def getYMDStr(timeStamp: String): String = {
    formatter_ymd.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp.toLong), ZoneId.systemDefault()))
  }

  def getCurYMD: String = {
    LocalDate.now().format(formatter_ymd)
  }

  def getCurrStamp: Long = {
    LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))
  }

  def main(args: Array[String]): Unit = {
    println(getYMDStr("1560151988"))
  }

}
