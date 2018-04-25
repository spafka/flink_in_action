package org.spafka.streaming.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

public class wordcount {


    public static void main(String[] args) throws Exception {

        ExecutionEnvironment flink = ExecutionEnvironment.getExecutionEnvironment();

        flink.fromElements("wab liab lab  lab labdub")
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {


                        for (String s1 : s.split(" ")) {
                            collector.collect(new Tuple2<String, Integer>(s1, 1));
                        }
                    }
                }).groupBy(0).reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> t1, Tuple2<String, Integer> t2) throws Exception {
                return new Tuple2<>(t1.f0, t1.f1 + t2.f1);
            }
        }).print();


    }

}
