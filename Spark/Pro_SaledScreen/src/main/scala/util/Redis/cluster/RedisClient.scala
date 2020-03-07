package util.Redis.cluster

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.log4j.Logger
import redis.clients.jedis.JedisPool

object RedisClient {
  val logger: Logger = Logger.getLogger(RedisClient.getClass)
  lazy val jedisPool: JedisPool = new JedisPool(new GenericObjectPoolConfig(),"10.149.1.150",6379,300000)

  def get(key: Array[Byte]): Array[Byte] = {
    val jedis = jedisPool.getResource
    val result: Array[Byte] = jedis.get(key)
    jedis.close()
    result
  }

  def set(key: Array[Byte], value: Array[Byte]): Boolean = {
    try {
      val jedis = jedisPool.getResource
      jedis.set(key, value)
      jedis.close()
      true
    } catch {
      case e: Exception => {
        logger.error(s"写入数据到Redis出错: ${e}")
        false
      }
    }
  }
}
