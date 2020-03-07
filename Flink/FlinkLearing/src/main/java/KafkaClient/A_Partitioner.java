package KafkaClient;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.Properties;

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 使用默认的分区器：
 * 若键为 null，消息将以 Round Robin 的方式，均匀地发送到主题的各个分区
 * 若键不为 null，Kafka 将计算键的散列值，映射到特定的分区
 * 支持自定义的分区逻辑，需要实现 org.apache.kafka.clients.producer.Partitioner 接口
 */
public class A_Partitioner implements Partitioner {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "KafkaClient.A_Partitioner");

    }

    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        if(!(o instanceof Long)){
            throw new IllegalArgumentException();
        }
        return (int) ( (Long)o %2);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
