package Methmods

import java.sql.DriverManager

object SparkJDBCThriftServerDemo {
  def main(args: Array[String]): Unit = {
    //1.添加驱动包驱动类
    val driver = "org.apache.hive.jdbc.HiveDriver"
    Class.forName(driver)

    //2.构建连接对象
    val url = "jdbc:hive2://10.149.1.31:10000/test"
    val conn = DriverManager.getConnection(url,"root","yadae.com")

    //3.sql语句执行
    conn.prepareStatement("use dw").execute()

    var pstmt = conn.prepareStatement("show tables")

    var rs = pstmt.executeQuery()

//    while (rs.next()){
//      println(s"Num =${rs.getInt("empno")},Name = ${rs.getString("ename")},sal = ${rs.getDouble("sal")}")
//    }

    rs.close()
    pstmt.close()
    println("===================================================")

//    pstmt = conn.prepareStatement("select empno,ename,sal from emp where sal > ?")
//    pstmt.setDouble(1,1500)
//    rs = pstmt.executeQuery()
//    while (rs.next()){
//      println(s"Num =${rs.getInt("empno")},Name = ${rs.getString("ename")},sal = ${rs.getDouble("sal")}")
//    }
//    rs.close()
//    pstmt.close()
//    conn.close()

  }

}
