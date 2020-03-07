import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
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
    public void testCreateTable() {
        //创建列集合
        ArrayList<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.INT32).key(true).build());
        columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.STRING).nullable(true).build());

        //创建表结构
        Schema schema = new Schema(columnSchemas);

        //建表选项
        CreateTableOptions createTableOptions = new CreateTableOptions();
        List<String> hashKeys = new ArrayList<String>();
        hashKeys.add("keys");

        //分区数
        int numBuckets = 8;
        createTableOptions.addHashPartitions(hashKeys,numBuckets);
    }

}
