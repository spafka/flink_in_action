package org.spafka.streaming.flink;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;

public class JoinTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<Integer, Integer>> input1 = env.fromElements(2, 4, 5, 6, 9, 11).map(new MapFunction<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> map(Integer integer) throws Exception {
                return Tuple2.of(integer % 4, integer);
            }
        }).assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<Tuple2<Integer, Integer>>() {
            @Nullable
            @Override
            public Watermark checkAndGetNextWatermark(Tuple2<Integer, Integer> lastElement, long extractedTimestamp) {
                return new Watermark(extractedTimestamp);
            }

            @Override
            public long extractTimestamp(Tuple2<Integer, Integer> element, long previousElementTimestamp) {
                return element.f1;
            }
        });


        DataStream<Tuple2<Integer, Integer>> input2 = env.fromElements(2, 4, 5, 6, 9, 11).map(new MapFunction<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> map(Integer integer) throws Exception {
                return Tuple2.of(integer % 4, integer);
            }
        }).assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<Tuple2<Integer, Integer>>() {
            @Nullable
            @Override
            public Watermark checkAndGetNextWatermark(Tuple2<Integer, Integer> lastElement, long extractedTimestamp) {
                return new Watermark(extractedTimestamp);
            }

            @Override
            public long extractTimestamp(Tuple2<Integer, Integer> element, long previousElementTimestamp) {
                return element.f1;
            }
        });

        DataStream<Tuple2<Integer, Integer>> input3 = env.fromElements(2, 4, 5, 6, 9, 11).map(new MapFunction<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> map(Integer integer) throws Exception {
                return Tuple2.of(integer % 4, integer);
            }
        }).assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<Tuple2<Integer, Integer>>() {
            @Nullable
            @Override
            public Watermark checkAndGetNextWatermark(Tuple2<Integer, Integer> lastElement, long extractedTimestamp) {
                return new Watermark(extractedTimestamp);
            }

            @Override
            public long extractTimestamp(Tuple2<Integer, Integer> element, long previousElementTimestamp) {
                return element.f1;
            }
        });

        DataStream<Tuple2<Integer, Integer>> firstJoin = input1.join(input2).where(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).equalTo(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).window(TumblingEventTimeWindows.of(Time.milliseconds(5))).apply(new JoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> join(Tuple2<Integer, Integer> integerIntegerTuple2, Tuple2<Integer, Integer> integerIntegerTuple22) throws Exception {
                return Tuple2.of(integerIntegerTuple2.f0, integerIntegerTuple2.f1 + integerIntegerTuple22.f1);
            }
        });

        DataStream<Tuple2<Integer, Integer>> secondJoin = firstJoin.join(input3).where(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).equalTo(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).window(TumblingEventTimeWindows.of(Time.milliseconds(5))).apply(new JoinFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> join(Tuple2<Integer, Integer> integerIntegerTuple2, Tuple2<Integer, Integer> integerIntegerTuple22) throws Exception {
                return Tuple2.of(integerIntegerTuple2.f0, integerIntegerTuple22.f1 + integerIntegerTuple2.f1);
            }
        });

        secondJoin.print();

        env.execute();
    }
}
