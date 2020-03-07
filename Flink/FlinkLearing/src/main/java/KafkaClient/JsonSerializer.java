package KafkaClient;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.Properties;


/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 自定义数据序列化
 */
public class JsonSerializer implements Serializer {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        return JSON.toJSONString(o).getBytes();
    }

    @Override
    public void close() {

    }

    public static void main(String[] args) {

        //Kafka 提供了 “序列化器” 的标准接口 org.apache.kafka.common.serialization.Serializer，允许实现自定义的数据序列化：
        Properties properties = new Properties();
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "KafkaClient.JsonSerializer");

    }
}
