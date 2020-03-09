import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.junit.Test;

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
        createTableOptions.addHashPartitions(hashKeys,numBuckets);

        //创建KUDU客户端
        String KUDU_MASTERS = "10.149.1.203:7051";
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
        client.createTable("test1",schema,createTableOptions);
        System.out.println("创建成功");
    }

}
