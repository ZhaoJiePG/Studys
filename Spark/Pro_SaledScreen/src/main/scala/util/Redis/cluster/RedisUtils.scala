package util.Redis.cluster

import java.{lang, util}

import com.alibaba.fastjson.JSONArray
import org.apache.spark.streaming.kafka010.OffsetRange
import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPoolConfig}

import scala.collection.mutable

object RedisUtils extends Serializable {

  def main(args: Array[String]): Unit = {
    set("k1", "v1")
  }

  private lazy val jedisClusterNodes = new util.HashSet[HostAndPort]()
  jedisClusterNodes.add(new HostAndPort("10.149.1.150", 6379))
  jedisClusterNodes.add(new HostAndPort("10.149.1.150", 6380))
  jedisClusterNodes.add(new HostAndPort("10.149.1.150", 6381))
  jedisClusterNodes.add(new HostAndPort("10.149.1.220", 6379))
  jedisClusterNodes.add(new HostAndPort("10.149.1.220", 6380))
  jedisClusterNodes.add(new HostAndPort("10.149.1.220", 6381))
  private val poolConfig = new JedisPoolConfig()
  poolConfig.setMaxTotal(500)
  poolConfig.setMaxIdle(50)
  poolConfig.setNumTestsPerEvictionRun(1024)
  poolConfig.setTimeBetweenEvictionRunsMillis(20000)
  poolConfig.setMinEvictableIdleTimeMillis(10000)
  poolConfig.setSoftMinEvictableIdleTimeMillis(10000)
  poolConfig.setMaxWaitMillis(1500)
  poolConfig.setTestOnBorrow(true)
  poolConfig.setTestWhileIdle(true)
  poolConfig.setTestOnReturn(true)

  var clients = new JedisCluster(jedisClusterNodes, poolConfig)

  /**
    *
    * @param key
    * @param value
    * @return
    */
  def set(key: String, value: String): Unit = {
    clients.set(key, value)
  }

  /**
    *
    * @param key
    * @return
    */
  def get(key: String): Option[String] = {
    val value: String = clients.get(key)
    Option(value)
  }

  /**
    *
    * @param key
    */
  def del(key: String): Unit = {
    clients.del(key)
  }

  /**
    *
    * @param hkey
    * @param key
    * @param value
    * @return
    */
  def hset(hkey: String, key: String, value: String): Boolean = {
    clients.hset(hkey, key, value) == 1
  }

  /**
    *
    * @param hkey
    * @param key
    * @return
    */
  def hget(hkey: String, key: String): Option[String] = {
    val value: String = clients.hget(hkey, key)
    Option(value)
  }

  /**
    *
    * @param hkey
    * @param key
    * @return
    */
  def hdel(hkey: String, key: String): Option[Long] = {
    Some(clients.hdel(hkey, key))
  }

  /**
    *
    * @param hkey
    * @param map
    */
  def hmset(hkey: String, map: mutable.Map[String, String]): Unit = {
    clients.hmset(hkey, map.asInstanceOf[java.util.Map[String, String]])
  }

  /**
    *
    * @param key
    * @param value
    * @return
    */
  def rpush(key: String, value: String): Option[Long] = {
    Some(clients.rpush(key, value))
  }

  /**
    *
    * @param key
    * @return
    */
  def lpop(key: String): Option[String] = {
    val value: String = clients.lpop(key)
    Option(value)
  }

  /**
    *
    * @param key
    * @return
    */
  def lhead(key: String): Option[String] = {
    val head: String = clients.lindex(key, 0)
    Option(head)
  }

  /**
    *
    * @param key
    * @return
    */
  def incr(key: String): Option[Long] = {
    val inc: lang.Long = clients.incr(key)
    Option(inc)
  }

  /**
    *
    * @param key
    * @param time
    * @return
    */
  def expire(key: String, time: Int) = {
    clients.expire(key, time)
  }

  /**
    * 根据key,获取value
    *
    * @param key
    * @return
    */
  def ttl(key: String): Option[Long] = {
    Some(clients.ttl(key))
  }

  /**
    * 获取上一次所有offset
    *
    * @param topic
    * @return
    */
  def getOffsetFromRedis(topic: String): collection.mutable.Map[String, String] = {
    if (null == clients) clients = new JedisCluster(jedisClusterNodes, poolConfig)
    val topicAndOffset: util.Map[String, String] = clients.hgetAll(topic)
    if (null == topicAndOffset || topicAndOffset.size() == 0) {
      return null
    }
    import scala.collection.JavaConverters._
    val offsetMap: collection.mutable.Map[String, String] = mapAsScalaMapConverter(topicAndOffset).asScala
    offsetMap
  }

  /**
    * 保存每次offset
    *
    * @param offsetRange
    */
  def saveOffset2Redis(offsetRange: Array[OffsetRange]) {
    if (offsetRange.length > 0) {
      if (null == clients) clients = new JedisCluster(jedisClusterNodes, poolConfig)
      offsetRange.foreach(t => {
        if (null != t) {
          clients.hset("topic", t.topic + "_" + t.partition.toString, t.untilOffset.toString)
        }
      })
    }
  }

  /**
    * 暂存每次check之后所有offset
    *
    * @param offsetInfo
    */
  def saveCheckedOffsetInfo2Redis(offsetInfo: Seq[(String, Int, Long)]) {
    if (offsetInfo.nonEmpty) {
      if (null == clients) clients = new JedisCluster(jedisClusterNodes, poolConfig)
      offsetInfo.foreach(t => {
        clients.hset("topic", t._1 + "_" + t._2.toString, t._3.toString)
      })
    }
  }

  /**
    * 储存网点分布数据到redis
    *
    * @param Seq ["netStore","key","JSON"]
    */
  def saveNetStoreInfo2Redis(offsetInfo: (String, String, JSONArray)) {
    if (null == clients) clients = new JedisCluster(jedisClusterNodes, poolConfig)
    clients.hset(offsetInfo._1, offsetInfo._2, offsetInfo._3.toString)
  }

}




