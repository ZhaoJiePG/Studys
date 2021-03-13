package wordcount;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Created by ZJ on 2021/3/9
 * comment:DataStream实现WordCount
 */
public class DataStreamWordCount {
    public static void main(String[] args) throws Exception {
        //0.env
//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //指定使用批还是流
//        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        //1.source
        DataStream<String> lineDatas = env.fromElements("a b c d e f g a b c d e f g");

        //2.transformation
        //切割
        DataStream<String> words = lineDatas.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] arr = s.split(" ");
                for (String word : arr) {
                    collector.collect(word);
                }
            }
        });

        //记为1
        DataStream<Tuple2<String, Integer>> wordAndOne = words.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return Tuple2.of(s, 1);
            }
        });

        //分组
        KeyedStream<Tuple2<String, Integer>,String> grouped = wordAndOne.keyBy(t -> t.f0);

        //聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = grouped.sum(1);

        //3.sink
        result.print();

        //4.启动并等待程序停止
        env.execute("Test");

    }
}
