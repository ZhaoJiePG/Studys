import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by ZJ on 2021-2-12
 * comment:测试kudu
 */
public class TestKudu {

    //生命全局变量，方便后续增删改查操作
    private KuduClient kuduClient;

    //kudu的master地址
    private String kuduMaster;

    //kudu中的表名
    private String kuduTable;

    //初始化方法，用于kedu集群建立连接
    @Before
    public void init(){

        //指定kudu集群master
        kuduMaster = "hadoopprd03:7051,hadoopprd04:7051,hadoopprd05:7051";

        //指定需要操作的表名
        kuduTable = "student";

        //创建kuduclientbuilder
        KuduClient.KuduClientBuilder kuduClientBuilder = new KuduClient.KuduClientBuilder(kuduMaster);

        //指定客户端kudu集群超时时间
        kuduClientBuilder.defaultAdminOperationTimeoutMs(10000);

        //通过builder中的build方法创建kuduclient
        kuduClient = kuduClientBuilder.build();

    }

    //关闭客户端
    @After
    public void close(){
        //如果客户端为关闭 执行close操作
        if(kuduClient != null){
            try {
                kuduClient.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建表
     */
    @Test
    public void createTable() throws KuduException {
       //判断表是否存在
       if(!kuduClient.tableExists(kuduTable)){

           //指定schema信息
           ArrayList<ColumnSchema> columnSchemas = new ArrayList<>();
           columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id",Type.INT32).key(true).build());
           columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name",Type.STRING).key(false).build());
           columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("age",Type.INT32).key(false).build());
           columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("sex",Type.INT32).key(false).build());
           Schema schema = new Schema(columnSchemas);

           //指定分区option属性
           ArrayList<String> partitionArray = new ArrayList<>();
           partitionArray.add("id");
           CreateTableOptions createTableOptions = new CreateTableOptions();
           //才有hash分区，指定6个分区
           createTableOptions.addHashPartitions(partitionArray,6);

           //如果表不存在，进行创建操作
           kuduClient.createTable(kuduTable,schema,createTableOptions);
       }else {
           kuduClient.deleteTable(kuduTable);
       }
    }

    /**
     * 增加数据
     */
    @Test
    public void insertTable() throws KuduException {
        //向表加载一个kudusession对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        //需要使用kuduTable来构建Operation的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(this.kuduTable);

        for (int i=1;i<=10;i++){
            Insert insert = kuduTable.newInsert();
            PartialRow row = insert.getRow();
            row.addInt("id",i);
            row.addString("name","test-"+i);
            row.addInt("age",20+i);
            row.addInt("sex",i%2);
            kuduSession.apply(insert);
        }
    }

    /**
     * 查询数据
     */
    @Test
    public void queryData() throws KuduException {

        //构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder = kuduClient.newScannerBuilder(kuduClient.openTable(kuduTable));
        ArrayList<String> columnlist = new ArrayList<>();
        columnlist.add("id");
        columnlist.add("name");
        columnlist.add("age");
        columnlist.add("sex");
        kuduScannerBuilder.setProjectedColumnNames(columnlist);

        //返回数据集
        KuduScanner kuduScanner = kuduScannerBuilder.build();

        //遍历
        while (kuduScanner.hasMoreRows()){
            RowResultIterator rowResults = kuduScanner.nextRows();

            while (rowResults.hasNext()){
                RowResult row = rowResults.next();
                int id = row.getInt("id");
                String name = row.getString("name");
                int age = row.getInt("age");
                int sex = row.getInt("sex");
                System.out.println(id+"=="+name+"=="+age+"=="+sex);
            }
        }
    }

    /**
     * 修改数据
     */
    @Test
    public void updateData() throws KuduException {

        //向表加载一个kudusession对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        //需要使用kuduTable来构建Operation的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(this.kuduTable);

        Upsert update = kuduTable.newUpsert();
        PartialRow row = update.getRow();
        row.addInt("id",9);
        row.addString("name","test");
        row.addInt("age",20);
        row.addInt("sex",2);
        kuduSession.apply(update);

    }

    /**
     * 删除数据
     */
    @Test
    public void deleteData() throws KuduException {


        //向表加载一个kudusession对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        //需要使用kuduTable来构建Operation的子类实例对象
        KuduTable kuduTable = kuduClient.openTable(this.kuduTable);

        Delete delete = kuduTable.newDelete();
        PartialRow row = delete.getRow();
        row.addInt("id",9);
        kuduSession.apply(delete);

    }

    /**
     * 测试分区
     */
    @Test
    public void testPartition() throws KuduException {
//        kuduClient.deleteTable("t_range_partition");

        //判断表是否存在
        if(!kuduClient.tableExists("t_mutil_partition")){

            //指定schema信息
            ArrayList<ColumnSchema> columnSchemas = new ArrayList<>();
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id",Type.INT32).key(true).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name",Type.STRING).key(false).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("age",Type.INT32).key(false).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("sex",Type.INT32).key(false).build());
            Schema schema = new Schema(columnSchemas);

            //指定用于分区option属性
            ArrayList<String> partitionArray = new ArrayList<>();
            //指定用于分区的字段
            partitionArray.add("id");
            CreateTableOptions createTableOptions = new CreateTableOptions();
            //才有hash分区，指定6个分区
            createTableOptions.addHashPartitions(partitionArray,3);
            //指定分区字段
            createTableOptions.setRangePartitionColumns(partitionArray);
            //指定分区策略
            /**
             * 0 <= id < 10
             * 10 <= id < 20
             * 20 <= id < 30
             * 30 <= id < 40
             * 40 <= id < 50
             */
            int count = 0 ;
            for (int i=0;i<5;i++){
                //指定range的上届和下届
                PartialRow lower = schema.newPartialRow();
                lower.addInt("id",count);
                count += 10 ;
                PartialRow upper = schema.newPartialRow();
                upper.addInt("id",count);
                createTableOptions.addRangePartition(lower,upper);
            }

            //如果表不存在，进行创建操作
            kuduClient.createTable("t_mutil_partition",schema,createTableOptions);
        }else {

        }
    }
}
