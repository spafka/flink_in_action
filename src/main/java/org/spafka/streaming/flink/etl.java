package org.spafka.streaming.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class etl {

    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment flink = StreamExecutionEnvironment.getExecutionEnvironment();

        SplitStream<Tuple2<String, Integer>> split = flink.readTextFile("readme.md")
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) {

                        String[] split = s.split(" ");

                        for (String s1 : split) {
                            collector.collect(new Tuple2<String, Integer>(s1, 1));
                        }

                    }
                }).keyBy(0)
                .sum(1).split(new OutputSelector<Tuple2<String, Integer>>() {
                    @Override
                    public Iterable<String> select(Tuple2<String, Integer> tuple2) {

                        List<String> s = new ArrayList<String>();
                        if (tuple2.f1 > 5) {
                            s.add("more");
                        } else {
                            s.add("less");
                        }
                        return s;

                    }
                });


        split.select("more").map(new MapFunction<Tuple2<String, Integer>, Object>() {
            @Override
            public Object map(Tuple2<String, Integer> tuple2) {

                System.out.println("more : " + tuple2);
                return null;
            }
        });
        split.select("less").map(new MapFunction<Tuple2<String, Integer>, Object>() {
            @Override
            public Object map(Tuple2<String, Integer> tuple2) {

                System.out.println("less " + tuple2);
                return null;
            }
        });
        flink.execute("splitDemo");

    }
}
