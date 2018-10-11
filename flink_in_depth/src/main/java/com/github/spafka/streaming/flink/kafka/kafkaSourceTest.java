package com.github.spafka.streaming.flink.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

@Slf4j
public class kafkaSourceTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fallBackRestart());
        env.enableCheckpointing(60000);// create a checkpoint every 5 seconds
        //   env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // @org.apache.flink.runtime.security.modules.JaasModule
        // @/**
        //################################################################################
        //#  Licensed to the Apache Software Foundation (ASF) under one
        //#  or more contributor license agreements.  See the NOTICE file
        //#  distributed with this work for additional information
        //#  regarding copyright ownership.  The ASF licenses this file
        //#  to you under the Apache License, Version 2.0 (the
        //#  "License"); you may not use this file except in compliance
        //#  with the License.  You may obtain a copy of the License at
        //#
        //#      http://www.apache.org/licenses/LICENSE-2.0
        //#
        //#  Unless required by applicable law or agreed to in writing, software
        //#  distributed under the License is distributed on an "AS IS" BASIS,
        //#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        //#  See the License for the specific language governing permissions and
        //# limitations under the License.
        //################################################################################
        //# We are using this file as an workaround for the Kafka and ZK SASL implementation
        //# since they explicitly look for java.security.auth.login.config property
        //# Please do not edit/delete this file - See FLINK-3929
        //**/
        //KafkaClient {
        //  org.apache.kafka.common.security.plain.PlainLoginModule required
        //  username = "admin"
        //  password = "admin";
        //};

        // 修改runtime下的配置文件，使之生产一个默认的配置文件
        // System.setProperty("java.security.auth.login.config", "/Users/spafka/Desktop/flink_in_action/src/main/resources/kafka_client_jaas.conf");

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("client.id", "flinkKafkaSource");
        props.put("group.id", "flink");

        // security
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");

        FlinkKafkaConsumer010<String> kafkaConsumer010 = new FlinkKafkaConsumer010<>(
                "testGo",
                new SimpleStringSchema(), props
        );

        kafkaConsumer010.setStartFromEarliest();
        DataStreamSource<String> input = env.addSource(kafkaConsumer010, "kafka_ev");

        input.map(new MapFunction<String, Object>() {
            @Override
            public Object map(String s) {
                log.info("message={}", s);
                return null;
            }
        });

        env.execute("test");


    }
}
