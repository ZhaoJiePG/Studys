package com.yadea.util

object BroadCastMapData {

  def main(args: Array[String]): Unit = {
    println(getFaultsInfoMap)
  }

  def getFaultsInfoMap: Map[String, (String, String)] = {
    Map("10" -> ("MOS 故障", "电机控制器"),
      "11" -> ("转把故障", "电机控制器"),
      "12" -> ("缺相故障", "电机控制器"),
      "13" -> ("霍尔故障", "电机控制器"),
      "14" -> ("刹把故障", "电机控制器"),
      "15" -> ("电机故障", "电机控制器"),
      "16" -> ("通信异常", "电机控制器"),
      "17" -> ("电机过流保护/堵转保护", "电机控制器"),
      "18" -> ("过压保护", "电机控制器"),
      "19" -> ("欠压保护", "电机控制器"),
      "1A" -> ("过温保护", "电机控制器"),
      "30" -> ("g-sensor 故障", "中控"),
      "31" -> ("E2ROM 故障", "中控"),
      "32" -> ("BLE 故障", "中控"),
      "33" -> ("RKE 故障", "中控"),
      "34" -> ("PKE 故障", "中控"),
      "35" -> ("功放故障", "中控"),
      "36" -> ("中控通讯故障", "中控"),
      "37" -> ("BMS 通讯故障", "中控"),
      "38" -> ("控制器通讯故障", "中控"),
      "39" -> ("仪表通讯故障", "中控"),
      "51" -> ("电池欠压", "电池"),
      "52" -> ("电池放电过流", "电池"),
      "53" -> ("电芯高温/放电过温故障", "电池"),
      "54" -> ("电芯低温/放电欠温故障", "电池"),
      "55" -> ("电池充电过温", "电池"),
      "56" -> ("电池充电欠温", "电池"),
      "57" -> ("电池充电过压", "电池"),
      "58" -> ("电池充电过流", "电池"),
      "70" -> ("前左转灯故障", "灯光"),
      "71" -> ("后左转灯故障", "灯光"),
      "72" -> ("前右转灯故障", "灯光"),
      "73" -> ("后右转灯故障", "灯光"),
      "74" -> ("近光灯故障", "灯光"),
      "75" -> ("远光灯故障", "灯光"),
      "76" -> ("后尾灯故障", "灯光"),
      "77" -> ("背景灯故障", "灯光"),
      "90" -> ("背光LED 灯故障", "仪表"),
      "91" -> ("图像驱动芯片故障", "仪表"),
      "92" -> ("过温故障", "仪表"),
      "93" -> ("过流故障", "仪表"),
      "94" -> ("过压故障", "仪表"),
      "95" -> ("欠压故障", "仪表"),
      "A0" -> ("DC故障", "其他"),
      "A1" -> ("喇叭故障", "其他"),
      "A2" -> ("扬声器故障", "其他")
    )
  }

  def getAlarmTypeMap: Map[Int, String] = {
    Map(0 -> "震动", 1 -> "断电", 2 -> "非法启动", 3 -> "超出围栏", 4 -> "倾倒", 5 -> "碰撞")
  }

  def getWarningTypeMap: Map[Int, String] = {
    Map(0 -> "Gsensor", 1 -> "GPS", 2 -> "车载通讯", 3 -> "超出围栏")
  }

  // 旧协议下故障信息
  def getOldFaultMap: Map[String, Map[Int, (String, String, String)]] = {
    Map("motorFault" -> Map(0 -> ("MOS 故障", "电机类故障", "motorFault")
      , 1 -> ("转把故障", "电机类故障", "motorFault")
      , 2 -> ("缺相故障", "电机类故障", "motorFault")
      , 3 -> ("霍尔故障", "电机类故障", "motorFault")
      , 4 -> ("刹把故障", "电机类故障", "motorFault")
      , 5 -> ("电机故障", "电机类故障", "motorFault")
      , 7 -> ("ECU通信异常", "电机类故障", "motorFault"))
      , "ctrlFault" -> Map(0 -> ("保留", "中控类故障", "ctrlFault")
        , 1 -> ("保留", "中控类故障", "ctrlFault")
        , 2 -> ("保留(不能使用)", "中控类故障", "ctrlFault")
        , 3 -> ("BLE故障", "中控类故障", "ctrlFault")
        , 4 -> ("保留(不能使用)", "中控类故障", "ctrlFault")
        , 5 -> ("防盗器故障", "中控类故障", "ctrlFault")
        , 6 -> ("控制器防盗器通讯故障", "中控类故障", "ctrlFault")
        , 7 -> ("防盗器GPS通讯故障", "中控类故障", "ctrlFault"))
      , "batteryFault" -> Map(0 -> ("BMS通信故障", "电池类故障", "batteryFault")
        , 1 -> ("过充故障", "电池类故障", "batteryFault")
        , 2 -> ("电芯高温故障", "电池类故障", "batteryFault")
        , 3 -> ("电芯低温故障", "电池类故障", "batteryFault")
        , 4 -> ("电池欠压", "电池类故障", "batteryFault"))
      , "gpsFault" -> Map(0 -> ("GPS通信故障", "GPS故障", "gpsFault")
        , 1 -> ("GPS信号异常", "GPS故障", "gpsFault")
        , 2 -> ("GSM信号异常", "GPS故障", "gpsFault"))
      , "otherFault" -> Map(0 -> (" ", "其他类型故障", "otherFault")
        , 1 -> ("DC异常", "其他类型故障", "otherFault")
        , 2 -> ("喇叭故障", "其他类型故障", "otherFault")
        , 3 -> ("防盗器故障", "其他类型故障", "otherFault"))
      , "lightBreakFault" -> Map(0 -> ("前左转", "灯光断路故障", "lightBreakFault")
        , 1 -> ("后左转", "灯光断路故障", "lightBreakFault")
        , 2 -> ("前右转", "灯光断路故障", "lightBreakFault")
        , 3 -> ("后右转", "灯光断路故障", "lightBreakFault")
        , 4 -> ("近光", "灯光断路故障", "lightBreakFault")
        , 5 -> ("远光", "灯光断路故障", "lightBreakFault")
        , 6 -> ("尾灯", "灯光断路故障", "lightBreakFault")
        , 7 -> ("刹车灯", "灯光断路故障", "lightBreakFault")
        , 8 -> ("背景灯1", "灯光断路故障", "lightBreakFault")
        , 9 -> ("背景灯2", "灯光断路故障", "lightBreakFault")
        , 10 -> ("背景灯3", "灯光断路故障", "lightBreakFault"))
      , "lightShortFault" -> Map(0 -> ("前左转", "灯光短路故障", "lightShortFault")
        , 1 -> ("后左转", "灯光短路故障", "lightShortFault")
        , 2 -> ("前右转", "灯光短路故障", "lightShortFault")
        , 3 -> ("后右转", "灯光短路故障", "lightShortFault")
        , 4 -> ("近光", "灯光短路故障", "lightShortFault")
        , 5 -> ("远光", "灯光短路故障", "lightShortFault")
        , 6 -> ("尾灯", "灯光短路故障", "lightShortFault")
        , 7 -> ("刹车灯", "灯光短路故障", "lightShortFault")
        , 8 -> ("背景灯1", "灯光短路故障", "lightShortFault")
        , 9 -> ("背景灯2", "灯光短路故障", "lightShortFault")
        , 10 -> ("背景灯3", "灯光短路故障", "lightShortFault"))
      , "pherpFault" -> Map(0 -> ("保留", "保留", "pherpFault"))
      , "signalFault" -> Map(0 -> ("保留", "保留", "signalFault"))
    )
  }
}
