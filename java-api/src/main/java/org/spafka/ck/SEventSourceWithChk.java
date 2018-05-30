package org.spafka.ck;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 该类是带checkpoint的source算子
public class SEventSourceWithChk extends RichSourceFunction<Tuple4<Long, String, String, Integer>> implements ListCheckpointed<UDFState> {
    private Long count = 0L;
    private boolean isRunning = true;
    private String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWZYX0987654321";

    // 算子的主要逻辑，每秒钟向流图中注入10000个元组
    public void run(SourceContext<Tuple4<Long, String, String, Integer>> ctx) throws Exception {
        Random random = new Random();
        while(isRunning) {
            for (int i = 0; i < 10000; i++) {
                ctx.collect(Tuple4.of(random.nextLong(), "hello-" + count, alphabet, 1));
                count++;
            }
            Thread.sleep(1000);
        }
    }

    // 任务取消时调用
    public void cancel() {
        isRunning = false;
    }

    // 制作自定义快照
    public List<UDFState> snapshotState(long l, long ll) throws Exception {
        UDFState udfState = new UDFState();
        List<UDFState> listState = new ArrayList<UDFState>();


        udfState.setState(count);


        listState.add(udfState);
        return listState;
    }

    // 从自定义快照中恢复数据
    public void restoreState(List<UDFState> list) throws Exception {

        UDFState udfState = list.get(0);
        count = udfState.getState();
    }
}