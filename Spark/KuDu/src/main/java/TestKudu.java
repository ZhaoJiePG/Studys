import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.Test;
import org.spark_project.jetty.server.session.JDBCSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZJ on 2020-2-20
 * comment:测试kudu
 */
public class TestKudu {

    /**
     * 创建表
     */
    @Test
    public void testCreateTable() throws KuduException {
        //创建列集合
        ArrayList<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.INT32).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("value", Type.STRING).nullable(true).build());

        //创建表结构
        Schema schema = new Schema(columnSchemas);

        //建表选项
        CreateTableOptions createTableOptions = new CreateTableOptions();

        //添加分区
        List<String> hashKeys = new ArrayList<String>();
        hashKeys.add("key");

        //分区数
        int numBuckets = 8;

        //组装表选项
        createTableOptions.addHashPartitions(hashKeys, numBuckets);

        //创建KUDU客户端
        String KUDU_MASTERS = "10.149.1.203:7051";
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
        client.createTable("test1", schema, createTableOptions);
        System.out.println("创建成功");
    }

    /**
     * 数据插入
     */
    @Test
    public void testInsert() throws KuduException {
        //创建KUDU客户端
        String KUDU_MASTERS = "10.149.1.203:7051";
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();

        //打开表
        KuduTable kuduTable = client.openTable("test1");
        //创建一个回话
        KuduSession kuduSession = client.newSession();
        for (int i = 0; i < 10; i++) {
            Insert insert = kuduTable.newInsert();
            //新建插入对象
            PartialRow partialRow = insert.getRow();
            partialRow.addInt("key", i);
            //判断偶数
            if (i % 2 == 0) {
                partialRow.setNull("value");
            }else{
                partialRow.addString("value", "value" + i);
            }
            //执行插入
            kuduSession.apply(insert);
        }
        //关闭session
        kuduSession.close();
        if(kuduSession.countPendingErrors() != 0){
            //获取操作结果
            RowErrorsAndOverflowStatus result1 = kuduSession.getPendingErrors();
            if(result1.isOverflowed()){
                System.out.println("buffer溢出了");
            }
            RowError[] errors = result1.getRowErrors();
            for (RowError error:errors){
                System.out.println(error);
            }
        }
        System.out.println("插入成功");
    }

    /**
     * 数据查询
     */
    @Test
    public void testQuery() throws KuduException {
        //创建KUDU客户端
        String KUDU_MASTERS = "10.149.1.203:7051";
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();

        //打开表
        KuduTable table = client.openTable("test1");
        //3、创建scanner扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder = client.newScannerBuilder(table);
        //4、创建查询条件
        KuduPredicate filter = KuduPredicate.newComparisonPredicate(table.getSchema().getColumn("key"), KuduPredicate.ComparisonOp.GREATER_EQUAL, 1);
        //5、将查询条件加入到scanner中
        KuduScanner scanner = kuduScannerBuilder.addPredicate(filter).build();
        //6、获取查询结果
        while (scanner.hasMoreRows()){
            RowResultIterator rows = scanner.nextRows();
            while (rows.hasNext()){
                RowResult row = rows.next();
                Integer id = row.getInt("key");
                String name = row.getString("value");
                System.out.println(id+"---"+name);
            }
        }
        //7、关闭client
        client.close();
    }
}
