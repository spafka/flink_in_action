package com.github.spafka.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Slf4j
public class Launcher {
    public static StreamExecutionEnvironment init() {
        StreamExecutionEnvironment flink = StreamExecutionEnvironment.getExecutionEnvironment();

        boolean isLocalmode = isLocalmode();
        if (isLocalmode) {
            log.info("create local environment with web UI enanled.");
            Configuration configuration = new Configuration();
            configuration.setInteger("web.port", 8081);
            flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        } else {
            // 重试30次，否则一些bug而抛出的Ex,重启之后还会抛出Ex 会无限循环
            flink.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(30, 60000));
            flink.enableCheckpointing(10000);// create a checkpoint every 10 seconds
            flink.getCheckpointConfig().setCheckpointTimeout(60000);
            flink.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
            flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
            flink.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        }

        return flink;
    }

    public static boolean isLocalmode() {
        return "dev".equals(System.getProperties().getProperty("flink.env"));
    }
}
