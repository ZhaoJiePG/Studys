package com.yadea.util

import java.net.UnknownHostException

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.internal.Logging
import scalaj.http
import scalaj.http.Http

import scala.collection.mutable.ListBuffer

/**
  * Created by ZJ on 2019-6-27
  * comment:http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=31.225696563611,121.49884033194&output=json&pois=1&latest_admin=1&ak=K2WGZeDWlluoHpEpt5qo5Sx6VNyvffLB
  */
object AddrConvert extends Logging{
  def main(args: Array[String]): Unit = {
    val json = new JSONObject()
    val res = fix2Address(121.49884033193993,31.225696429417988,json)
  println(res)
  }

  /**
    * 根据经纬度获取百度地图接口的省市县等信息
    *
    * @param lon 经度
    * @param lat 纬度
    * @param input
    * @return
    */
    def fix2Address(lon:Double,lat:Double,input:JSONObject): (JSONObject, Seq[String], Seq[String], Seq[String], Seq[String],JSONObject)= try {
      //请求百度api
      val response: http.HttpResponse[String] = Http("http://api.map.baidu.com/geocoder/v2/")
        .timeout(10000, 60000)
        .charset("UTF-8")
        .header("Content-Type", "application/json")
        //.param("callback", "renderReverse")
        .param("location", lat + "," + lon)
        .param("output", "json").param("pois", "1")
        .param("latest_admin", "1").param("ak", "K2WGZeDWlluoHpEpt5qo5Sx6VNyvffLB").asString

      //保存结果的JSON字符串
      val result:JSONObject = new JSONObject()
      //请求没有数据返回null
      if(StringUtils.isBlank(response.body)) return null

      //获取json数据的主数据result
      val resultIter = JSON.parseObject(response.body).getJSONObject("result")

      //判断是否有addressComponent数据
      if(resultIter.containsKey("addressComponent")){
        result.put("addressComponent",resultIter.getOrDefault("addressComponent",""))
      }

      //判断是否有sematic_description数据
      if(resultIter.containsKey("sematic_description")){
        result.put("sematic_description",resultIter.getOrDefault("sematic_description",""))
      }

      //解析business
      val businessList: List[String] = if(resultIter.containsKey("business"))
        resultIter.getOrDefault("business","").toString.split(",").toList
      else List("")

      //解析addressComponent
      val addressComponentIter: JSONObject = resultIter.getJSONObject("addressComponent")
      result.put("country", addressComponentIter.getOrDefault("country", ""))
      result.put("province", addressComponentIter.getOrDefault("province", ""))
      result.put("city", addressComponentIter.getOrDefault("city", ""))
      result.put("district", addressComponentIter.getOrDefault("district", ""))
      result.put("street", addressComponentIter.getOrDefault("street", ""))
      result.put("town", addressComponentIter.getOrDefault("town", ""))

      val pois_name = new ListBuffer[String]
      val pois_type = new ListBuffer[String]
      val pois_tag = new ListBuffer[String]
      val poisJsonArray: JSONArray = resultIter.getJSONArray("pois")
      var poisSingle: JSONObject = null
      for (k <- 0 until poisJsonArray.size()) {
        poisSingle = poisJsonArray.getJSONObject(k)
        pois_name.+=(poisSingle.getOrDefault("name", "").toString)
        if (poisSingle.getString("tag").split(";").length > 1) {
          pois_type.+=(poisSingle.getString("tag").split(";")(0))
          pois_tag.+=(poisSingle.getString("tag").split(";")(1))
        }
      }
      (result, businessList, pois_name, pois_type, pois_tag,input)
    }catch {
      case exception1: UnknownHostException =>
        logError(exception1.getMessage)
        null
      case exception2: Exception =>
        logError(exception2.getMessage)
        null
    }

}


