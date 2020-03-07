package jdbc;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

public class DBManager {

    private static Logger logger = LogManager.getLogger(DBManager.class);

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DbPoolConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("DBManager getConnection null.{}", e);
            return null;
        }
        return connection;
    }

    /**
     * 链接指定的 数据库对象
     *
     * @param dataBaseName 对象名
     * @return Connection
     */
    public static Connection getConnection(String dataBaseName) {
        Connection conn = null;
        try {
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
            conn = DriverManager.getConnection("proxool." + dataBaseName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    public static void close(Connection conn, Statement st, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception e) {
            logger.error("DBManager close fail rs. {}", e);
        }

        try {
            if (st != null)
                st.close();
        } catch (Exception e) {
            logger.error("DBManager close fail st. {}", e);
        }

        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            logger.error("DBManager close fail conn. {}", e);
        }
    }


}
