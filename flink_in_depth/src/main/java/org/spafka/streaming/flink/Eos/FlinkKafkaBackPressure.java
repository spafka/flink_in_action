package org.spafka.streaming.flink.Eos;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.types.Nothing;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.joda.time.DateTime;
import org.spafka.common.Launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

@Slf4j
public class FlinkKafkaBackPressure {

    public static void main(String[] args) throws Exception {

        System.setProperty("flink.env", "dev");

        StreamExecutionEnvironment flink = Launcher.init();
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost" + ":" + 9092);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer2");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put("group.id", "test");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        FlinkKafkaConsumer010<Long> flinkKafkaConsumer010 = new FlinkKafkaConsumer010("test", new LongDeser(), props);

        DataStreamSource<Long> kafkaSource = flink.addSource(flinkKafkaConsumer010);

        kafkaSource.keyBy(new KeySelector<Long, Long>() {
            @Override
            public Long getKey(Long value) throws Exception {
                return value ;
            }
        }).timeWindow(Time.seconds(1)).process(new ProcessWindowFunction<Long, Nothing, Long, TimeWindow>() {
            @Override
            public void process(Long key, Context context, Iterable<Long> elements, Collector<Nothing> out) throws Exception {

                log.error("key={},v={}",key,Lists.newArrayList(elements));
            }
        });

        flink.execute("flink eos");


    }


    static class IntDeser implements DeserializationSchema<Integer> {

        @Override
        public Integer deserialize(byte[] data) throws IOException {
            if (data == null)
                return null;
            if (data.length != 4) {
                throw new SerializationException("Size of data received by IntegerDeserializer is not 4");
            }

            int value = 0;
            for (byte b : data) {
                value <<= 8;
                value |= b & 0xFF;
            }
            return value;
        }

        @Override
        public boolean isEndOfStream(Integer nextElement) {
            return false;
        }

        @Override
        public TypeInformation<Integer> getProducedType() {
            return TypeInformation.of(Integer.class);
        }
    }

    static class LongDeser implements DeserializationSchema<Long> {


        @Override
        public Long deserialize(byte[] data) throws IOException {
            if (data == null)
                return null;
            if (data.length != 8) {
                throw new SerializationException("Size of data received by LongDeserializer is not 8");
            }

            long value = 0;
            for (byte b : data) {
                value <<= 8;
                value |= b & 0xFF;
            }
            return value;
        }

        @Override
        public boolean isEndOfStream(Long nextElement) {
            return false;
        }

        @Override
        public TypeInformation<Long> getProducedType() {
            return TypeInformation.of(Long.class);
        }
    }


}
