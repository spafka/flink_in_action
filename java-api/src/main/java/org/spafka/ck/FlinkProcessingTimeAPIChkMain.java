package org.spafka.ck;

import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class FlinkProcessingTimeAPIChkMain {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置相关配置，并开启checkpoint功能
        env.setStateBackend(new FsStateBackend("file:///E:/flink-checkpoint/checkpoint/"));
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointInterval(6000);

        // 应用逻辑
        env.addSource(new SEventSourceWithChk())
                .keyBy(0)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(4), Time.seconds(1)))
                .apply(new WindowStatisticWithChk())
                .print();

        env.execute();
    }
}