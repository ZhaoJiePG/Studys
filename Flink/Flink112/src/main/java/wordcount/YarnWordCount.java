package wordcount;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * Created by ZJ on 2021/3/9
 * comment:DataStream实现WordCount
 */
public class YarnWordCount {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String outPut = "";
        if(parameterTool.has("outPut")){
            outPut = parameterTool.get("outPut");
        }else {
            System.out.println("可以指定输出路径 使用 --output，没有指定使用默认");
            outPut = "hdfs://xxxxxx";
        }

        //0.env
//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //指定使用批还是流
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);

        //1.source
        DataStream<String> lineDatas = env.fromElements("a b c d e f g a b c d e f g");

        //2.transformation
        //切割
//        DataStream<String> words = lineDatas.flatMap(new FlatMapFunction<String, String>() {
//            @Override
//            public void flatMap(String s, Collector<String> collector) throws Exception {
//                String[] arr = s.split(" ");
//                for (String word : arr) {
//                    collector.collect(word);
//                }
//            }
//        });
        //lamda表达式需要返回类型
        DataStream<String> words = lineDatas.flatMap((String in, Collector<String> out) -> {
            Arrays.stream(in.split(" ")).forEach(out::collect);
        }).returns(Types.STRING);

        //记为1
//        DataStream<Tuple2<String, Integer>> wordAndOne = words.map(new MapFunction<String, Tuple2<String, Integer>>() {
//            @Override
//            public Tuple2<String, Integer> map(String s) throws Exception {
//                return Tuple2.of(s, 1);
//            }
//        });
        DataStream<Tuple2<String, Integer>> wordAndOne = words.map((String value) -> Tuple2.of(value, 1))
                .returns(Types.TUPLE(Types.STRING,Types.INT));

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
