package com.xiaoxiang.flink.connect;

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

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class Join {
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

        DataStream<Tuple2<Integer, Integer>> secondJoin = firstJoin.join(input3).where(new KeySelector<Tuple2<Integer,Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).equalTo(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
            @Override
            public Integer getKey(Tuple2<Integer, Integer> integerIntegerTuple2) throws Exception {
                return integerIntegerTuple2.f0;
            }
        }).window(TumblingEventTimeWindows.of(Time.milliseconds(5))).apply(new JoinFunction<Tuple2<Integer,Integer>, Tuple2<Integer,Integer>, Tuple2<Integer, Integer>>() {
            @Override
            public Tuple2<Integer, Integer> join(Tuple2<Integer, Integer> integerIntegerTuple2, Tuple2<Integer, Integer> integerIntegerTuple22) throws Exception {



                return Tuple2.of(integerIntegerTuple2.f0, integerIntegerTuple22.f1 + integerIntegerTuple2.f1);
            }
        });

        secondJoin.print();

        env.execute();
    }
}
