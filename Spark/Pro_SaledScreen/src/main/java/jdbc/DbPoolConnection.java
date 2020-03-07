package jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class DbPoolConnection {

    private static DbPoolConnection dbPoolConnection = null;
    private static DruidDataSource druidDataSource = null;

    static {
        try {
            Properties properties = new Properties();
            properties.load(DbPoolConnection.class.getClassLoader().getResourceAsStream("resources/my.properties"));
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DbPoolConnection() {
    }

    public static synchronized DbPoolConnection getInstance() {
        if (null == dbPoolConnection) dbPoolConnection = new DbPoolConnection();
        return dbPoolConnection;
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }


    public static void main(String[] args) throws SQLException {
        PreparedStatement ps = DbPoolConnection.getInstance().getConnection().prepareStatement("select * from basic_call");
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnNum = rsmd.getColumnCount();
        while (rs.next()) {

            for (int i = 0; i < columnNum; i++) {
                System.out.println(rsmd.getColumnLabel(i + 1) + "|" +rs.getObject(i + 1));
            }
        }


    }

}
