package com.github.spafka.flink;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;
import java.util.regex.Pattern;

public class FlinkKafka {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.setInteger(JobManagerOptions.ADDRESS.key(), 8081);
        StreamExecutionEnvironment flink = StreamExecutionEnvironment.getExecutionEnvironment();

        flink.setMaxParallelism(3);

        // 每隔1000 ms进行启动一个检查点【设置checkpoint的周期】
        flink.enableCheckpointing(10000);
        // 高级选项：
        // 设置模式为exactly-once （这是默认值）
        flink.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
        flink.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        flink.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同一时间只允许进行一个检查点
        flink.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】
        flink.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        flink.setStateBackend(new RocksDBStateBackend("file:///D:/rocks"));

        // configure Kafka consumer
        Properties kafkaProps = new Properties();
        kafkaProps.setProperty("bootstrap.servers", "localhost:9092");
        kafkaProps.setProperty("group.id", "cep");

        FlinkKafkaConsumer<byte[]> consumer = new FlinkKafkaConsumer(
                Pattern.compile("test"), new ByteDerser(), kafkaProps);

        flink.addSource(consumer, "kafka").slotSharingGroup("kafka").setParallelism(3)
                .map(x -> new String(x)).slotSharingGroup("map").setParallelism(3)
                .addSink(new DiscardingSink<>()).slotSharingGroup("discard").setParallelism(3);

        flink.execute("flink in action ");

    }

    static class ByteDerser implements DeserializationSchema<byte[]> {


        @Override
        public byte[] deserialize(byte[] message) {
            return message;
        }

        @Override
        public boolean isEndOfStream(byte[] nextElement) {
            return false;
        }

        @Override
        public TypeInformation<byte[]> getProducedType() {
            return TypeInformation.of(new TypeHint<byte[]>() {
                @Override
                public TypeInformation<byte[]> getTypeInfo() {
                    return super.getTypeInfo();
                }
            });
        }
    }
}
