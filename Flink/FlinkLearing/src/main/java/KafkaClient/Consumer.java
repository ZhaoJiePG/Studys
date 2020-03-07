package KafkaClient;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * Created by ZJ on 2019/12/27
 * comment:
 * kafka消费者api
 */
public class Consumer {
    public static void main(String[] args) {

        //构建KafkaConsumer
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka02:9092,kafka03:9092,kafka04:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group_1");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("test");
        consumer.subscribe(topics);

        //订阅消息
        while (true){
            ConsumerRecords<String,String>  records = consumer.poll(100);
            for(ConsumerRecord<String,String>  record:records){
                System.out.println("topic:"+record.topic()+
                        ",partition:"+record.partition()+
                        ",offset:"+record.offset()+
                        ",key"+record.key()+
                        ",value:"+record.key());
            }
            //提交偏移量
            consumer.commitSync();
        }
    }
}
