package KafkaClient;

import org.apache.kafka.clients.*;
import org.apache.kafka.clients.producer.*;


import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by ZJ on 2019/12/23
 * comment:
 * 生产者api
 */
public class Producer {

    public static void main(String[] args) {
        // 构建 KafkaProducer
        Properties properties = new Properties();
        // kafka地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka02:9092,kafka03:9092,kafka04:9092");
        // key.serializer / value.serializer：Kafka 消息的键、值，仅支持字节数组，因此，需要提供键 & 值的 “序列化器”
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        // 构建 ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", "key_1", "value_1-" + System.currentTimeMillis());
        // 消息发送
        Future<RecordMetadata> result = producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println("message sent, partition = " + metadata.partition() + ", offset = " + metadata.offset());
                } else {
                    exception.printStackTrace(System.out);
                }
            }
        });
        // 等待发送完成
        //
        try {
            result.get();
        } catch (Exception e) {

        }
    }
}
