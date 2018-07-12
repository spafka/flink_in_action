package org.spafka.ck;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

// 该类是带checkpoint的window算子
public class WindowStatisticWithChk implements WindowFunction<Tuple4<Long, String, String, Integer>, Long, Tuple, TimeWindow>, ListCheckpointed<UDFState> {
    private Long total = 0L;

    // window算子实现逻辑，统计window中元组的个数
    @Override
    public void apply(Tuple key, TimeWindow window, Iterable<Tuple4<Long, String, String, Integer>> input,
                      Collector<Long> out) throws Exception {
        long count = 0L;
        for (Tuple4<Long, String, String, Integer> event : input) {
            count++;
        }
        total += count;
        out.collect(count);
    }

    // 制作自定义快照
    @Override
    public List<UDFState> snapshotState(long l, long ll) throws Exception {
        List<UDFState> listState = new ArrayList<UDFState>();
        UDFState udfState = new UDFState();
        udfState.setState(total);
        listState.add(udfState);
        return listState;
    }


    // 从自定义快照中恢复状态
    @Override
    public void restoreState(List<UDFState> list) throws Exception {
        UDFState udfState = list.get(0);
        total = udfState.getState();
    }
}