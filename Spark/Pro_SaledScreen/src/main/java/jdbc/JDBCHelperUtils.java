package jdbc;

import com.alibaba.fastjson.JSONObject;
import constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DateUtils;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JDBCHelperUtils implements Serializable {

    private static final Log log = LogFactory.getLog(JDBCHelperUtils.class);
    private static LinkedList<Connection> dataSources = new LinkedList<>();
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    static {
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            int dataSourceSize = 20;
            String mysqlUrl = "jdbc:mysql://10.149.1.154:3306/saled_screen?characterEncoding=utf-8";
            String mySqlUser = "root";
            String mysqlPassWord = "root";
            for (int i = 0; i < dataSourceSize; i++) {
                try {
                    Connection connection = DriverManager.getConnection(mysqlUrl, mySqlUser, mysqlPassWord);
                    dataSources.add(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     *
     * @return Connection
     */
    private static synchronized Connection getConnection() {
        while (dataSources.size() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(dataSources.size());
        return dataSources.poll();
    }

    /**
     * 根据分组类型, 删除分组结果
     *
     * @param type 业务数据类型 1:全国门店类型分布 | 2:全国销售数据
     * @param sql  执行sql
     */
    public void delStoreDistributeInfoByGroupOption(String sql, int type) {
        if (StringUtils.isBlank(sql)) return;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, type);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                dataSources.push(connection);
            }
        }
    }


    /**
     * 批量更新
     *
     * @param paramsList 参数
     */
    public void executeBatch(List<JSONObject> paramsList, String sql) {
        paramsList.forEach(k -> System.out.println(k.toJSONString()));
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            for (JSONObject params : paramsList) {
                preparedStatement.setInt(1, params.getIntValue("type"));
                preparedStatement.setString(2, params.getString("nation"));
                preparedStatement.setString(3, params.getString("privince"));
                preparedStatement.setString(4, params.getString("city"));
                preparedStatement.setString(5, params.getString("town"));
                preparedStatement.setString(6, params.getString("store_level"));
                preparedStatement.setInt(7, params.getIntValue("groupby_option"));
                preparedStatement.setInt(8, params.getIntValue("count"));
                preparedStatement.setString(9, params.getString("compute_date"));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                dataSources.push(connection);
            }
        }
    }

    /**
     * 呼叫中心计算结果入库
     *
     * @param columns 每一列value
     * @return 行数
     */
    public int updateBasicCallInfoById(Object[] columns) {
        int res = 0;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(Constants.BASIC_CALLL_UPDATE_SQL);
            for (int i = 0; i < columns.length; i++) {
                if (i == 1 || i == 2 || i == 4)
                    preparedStatement.setString(i + 1, columns[i].toString());
                else preparedStatement.setInt(i + 1, Integer.valueOf(columns[i].toString()));
            }
            preparedStatement.setString(63, DateUtils.getCurrentDateTimeStr());
            res = preparedStatement.executeUpdate();

            connection.commit();
        } catch (Exception e) {
            log.error("error updateBasicCallInfoById:" + Arrays.asList(columns).toString(), e);
            log.error(preparedStatement.toString());
        } finally {
            try {
                if (null == connection || !connection.isClosed())
                    dataSources.push(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }


    public static void main(String[] args) throws SQLException {
        JDBCHelperUtils jdbc = new JDBCHelperUtils();
    }

}
