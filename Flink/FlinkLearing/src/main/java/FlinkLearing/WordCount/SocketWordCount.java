package FlinkLearing.WordCount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Created by ZJ on 2019/12/25
 * comment:
 */
public class SocketWordCount {
    public static void main(String[] args) throws Exception {
        //检查参数
        if (args.length != 2){
            System.err.println("USAGE:\\nSocketTextStreamWordCount <hostname> <port>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //获取数据
        DataStreamSource<String> stream = env.socketTextStream(hostname, port);

        //计数
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = stream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String input, Collector<Tuple2<String, Integer>> output) throws Exception {
                String[] tokens = input.split("\\W+");
                for (String token : tokens) {
                    if (token.length() > 0) {
                        output.collect(new Tuple2<String, Integer>(token, 1));
                    }
                }
            }
        }).keyBy(0).sum(1);

        sum.print();

        env.execute("SocketWordCount");
    }
}
