package kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.ListOffsetRequest;
import org.apache.kafka.common.requests.ListOffsetResponse;
import org.apache.kafka.common.requests.MetadataRequest;
import org.apache.kafka.common.requests.MetadataResponse;

import java.net.InetSocketAddress;
import java.util.*;

public class KafkaTest {
    private static Map<String, Object> config = new HashMap<String, Object>();
    static {
    config.put("bootstrap.servers", "ydhadoop03:9092,ydhadoop01:9092,ydhadoop02:9092");
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "groupc");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", false);}


    public static void main(String[] args) {
//        List<InetSocketAddress> net = new ArrayList();
//        net.add(new InetSocketAddress("ydhadoop01", 9092));
//        net.add(new InetSocketAddress("ydhadoop02", 9092));
//        net.add(new InetSocketAddress("ydhadoop03", 9092));
//        Cluster cluster = Cluster.bootstrap(net);
//        cluster.partitionsForTopic("testopic1").forEach(a -> a.leader());
//
//        Consumer consumer = new KafkaConsumer(config);
//        Map<TopicPartition, OffsetAndMetadata> hashMaps = new HashMap<>();
//        hashMaps.put(new TopicPartition("testopic1", 0), new OffsetAndMetadata(0));
//        consumer.commitSync(hashMaps);
//        consumer.subscribe(Arrays.asList("testopic1"));
//        while (true) {
//            ConsumerRecords<String, Object> records = consumer.poll(100);
//            for (ConsumerRecord<String, Object> record : records) {
//                System.out.println(record.toString());
//            }
//        }

        Properties props = new Properties();
        props.put("bootstrap.servers", "ydhadoop02:9092");
        props.put("group.id", "test2");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        //consumer.subscribe(Arrays.asList("testopic1","testopic2"));
        consumer.subscribe(Collections.singletonList("yadea_gps_shanghai"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("----------------------------------------------------------------------");
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
                System.out.println("----------------------------------------------------------------------");
            }
        }
    }

}
