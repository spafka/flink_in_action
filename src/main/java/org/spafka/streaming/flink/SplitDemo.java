package org.spafka.streaming.flink;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SplitDemo {


    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setBufferTimeout(1);

        DataStream<Integer> d1 = env.fromElements(0, 2, 4, 6, 8);
        DataStream<Integer> d2 = env.fromElements(1, 3, 5, 7, 9);

        SplitStream<Integer> integerSplitStream = d1.split(new OutputSelector<Integer>() {

            @Override
            public Iterable<String> select(Integer value) {
                List<String> s = new ArrayList<String>();
                if (value > 4) {
                    s.add(">");
                } else {
                    s.add("<");
                }
                return s;
            }
        });

        integerSplitStream.select(">").writeAsText(">");
        integerSplitStream.select("<").writeAsText("<");


        env.execute();

    }
}
