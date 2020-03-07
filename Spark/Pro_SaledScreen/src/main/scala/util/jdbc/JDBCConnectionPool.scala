package util.jdbc

import java.io.InputStream
import java.sql.{Connection, DriverManager}
import java.util
import java.util.Properties

object JDBCConnectionPool extends Serializable {

  /**
    * 数据库连接池工具类
    */
  object DBConnectionPool {
    private val properties = new Properties()
    val inputStream: InputStream = this.getClass.getClassLoader.getResourceAsStream("jdbc.properties")
    properties.load(inputStream)
    inputStream.close()
    private val driver = properties.getProperty("jdbc.driver")
    private val url = properties.getProperty("jdbc.url")
    private val username = properties.getProperty("jdbc.username")
    private val password = properties.getProperty("jdbc.password")
    private val initialSize = properties.getProperty("jdbc.initialSize").toInt //初始化连接数|产生连接数
    private val maxActive = properties.getProperty("jdbc.maxActive").toInt //最大连接数量|连接池总数
    private val maxIdle = properties.getProperty("jdbc.maxIdle").toInt //最大连接数量
    private val minIdle = properties.getProperty("jdbc.minIdle").toInt //最小空闲连接
    private val maxWait = properties.getProperty("jdbc.maxWait").toInt //maxWait代表当Connection用尽了，多久之后进行回收丢失连接
    private var current_num = 0 //当前连接池已产生的连接数
    private val pools = new util.LinkedList[Connection]() //连接池

    /**
      * 加载驱动
      */
    private def before() {
      Class.forName(driver)
    }

    /**
      * 获得连接
      */
    private def initConn(): Connection = {
      val connection: Connection = DriverManager.getConnection(url, username, password)
      connection
    }

    /**
      * 初始化连接池
      */
    private def initConnectionPool(): util.LinkedList[Connection] = {
      AnyRef.synchronized({
        if (pools.isEmpty) {
          before()
          for (_ <- 1 to maxActive.toInt) {
            pools.push(initConn())
            current_num += 1
          }
        }
        pools
      })
    }

    /**
      * 获得连接
      */
    def getConnection: Connection = {
      initConnectionPool()
      pools.poll()
    }

    /**
      * 释放连接
      */
    def releaseCon(con: Connection) {
      pools.push(con)
    }
  }

}
