package com.github.spafka.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.*;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;


@Slf4j
public class FlinkServer {


    public static final String FLINK_ENV = "flink.env";


    public static StreamExecutionEnvironment initDev() throws IOException {
        System.setProperty(FLINK_ENV, "dev");
        return init();
    }

    public static StreamExecutionEnvironment init() throws IOException {

        final ParameterTool params = ParameterTool.fromSystemProperties();
        StreamExecutionEnvironment flink;

        boolean isLocalmode = isLocalmode();
        if (isLocalmode) {
            log.info("create local environment with web UI enanled.");
            Configuration configuration = getConfiguration();
//            configuration.setString(RocksDBOptions.LOCAL_DIRECTORIES, "file:///D://rocks");
            flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);

            flink.enableCheckpointing(10000);
            flink.getConfig().setGlobalJobParameters(params);
            flink.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

            flink.setStateBackend(new RocksDBStateBackend(configuration.getString(CheckpointingOptions.CHECKPOINTS_DIRECTORY)));
            flink.setParallelism(3);

        } else {
            // 60，否则一些bug而抛出的Ex,重启之后还会抛出Ex 会无限循环 //
            flink = StreamExecutionEnvironment.getExecutionEnvironment();
            // flink.setParallelism(3);
            flink.getConfig().setUseSnapshotCompression(true);
            // 重试7天 //
            flink.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(60 * 24 * 7, 60000));
            flink.enableCheckpointing(60000);
            flink.getCheckpointConfig().setCheckpointTimeout(60000);
            flink.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);
            flink.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
            flink.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

            // 默认的算子发送周期，较小可同时提供吞吐量与低延迟的权衡
            flink.setBufferTimeout(100L);
            flink.getConfig().setGlobalJobParameters(params);
            flink.getConfig().disableSysoutLogging();
        }

        return flink;
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setInteger("web.port", 8081);
        // akka //
        configuration.setString(AkkaOptions.ASK_TIMEOUT, "1000 s");
        configuration.setString(AkkaOptions.TCP_TIMEOUT, "1000 s");
        configuration.setString(AkkaOptions.LOOKUP_TIMEOUT, "1000 s");
        configuration.setString(AkkaOptions.CLIENT_TIMEOUT, "1000 s");
        // 被statebacked 覆盖
        configuration.setString(CheckpointingOptions.CHECKPOINTS_DIRECTORY, "file:///D://state/checkpoint");

        configuration.setString(CheckpointingOptions.SAVEPOINT_DIRECTORY, "file:///D://state/savepoint");
        configuration.setBoolean(CheckpointingOptions.ASYNC_SNAPSHOTS, true);
        configuration.setInteger(CheckpointingOptions.MAX_RETAINED_CHECKPOINTS, 10);
        configuration.setString("state.backend", "rocksdb");
        return configuration;
    }

    public static boolean isLocalmode() {
        return "dev".equals(System.getProperties().getProperty(FLINK_ENV));
    }


    public static void main(String[] args) throws Exception {


        Configuration configuration = getConfiguration();

        int numSlotsPerTaskManager = configuration.getInteger(TaskManagerOptions.NUM_TASK_SLOTS, 100);

        MiniClusterConfiguration cfg = new MiniClusterConfiguration.Builder()
                .setConfiguration(configuration)
                .setNumSlotsPerTaskManager(numSlotsPerTaskManager)
                .build();


        log.info("Running job on local embedded Flink mini cluster");

        MiniCluster miniCluster = new MiniCluster(cfg);

        try {
            miniCluster.start();
            configuration.setInteger(RestOptions.PORT, miniCluster.getRestAddress().getPort());
            // enter tab to break
            for (; System.in.read() >= 10; ) {
            }
        } finally {
            miniCluster.close();
        }


    }
}
