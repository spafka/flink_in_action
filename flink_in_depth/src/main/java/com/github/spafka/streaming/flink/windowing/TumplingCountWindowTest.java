/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.spafka.streaming.flink.windowing;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TumplingCountWindowTest {

    public static void main(String[] args) throws Exception {

        final ParameterTool params = ParameterTool.fromArgs(args);

        System.setProperty("flink.env", "dev");


        // set up the execution environment
        final StreamExecutionEnvironment flink = StreamExecutionEnvironment.getExecutionEnvironment();

        flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //  BoundedOutOfOrdernessTimestampExtractor 提取时间戳，并允许一定时间的延迟计算，延迟时间到之后，才会开始计算
        //  而 allowedLateness 则会在延迟期内，触发过之后，还会继续触发 process
        flink.addSource(new BuyerCount()).keyBy(0)
                .keyBy(1).countWindow(10).process(new ProcessWindowFunction<Tuple4<Long, Integer, Integer, Date>, Object, Tuple, GlobalWindow>() {
            @Override
            public void process(Tuple key, Context context, Iterable<Tuple4<Long, Integer, Integer, Date>> elements, Collector<Object> out) throws Exception {

                ArrayList<Tuple4<Long, Integer, Integer, Date>> list = Lists.newArrayList(elements);

                List<Integer> collect = list.stream().map(x -> x.f1).collect(toList());

                log.error("Thread= {},key ={} ,window= {} ,procreetime={},elements={} ", Thread.currentThread().getId(), key, context.window(), new DateTime(), collect);

            }
        });

        flink.execute("windowTest");

    }

}
