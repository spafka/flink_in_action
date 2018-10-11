package com.github.spafka.streaming.flink.windowing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;
import java.util.concurrent.atomic.LongAdder;

//id uid count time
@Slf4j
public class BuyerCount implements SourceFunction<Tuple4<Long, Integer, Integer, Date>> {

    private static final long serialVersionUID = 1L;


    private volatile boolean isRunning = true;


    @Override
    public void run(SourceFunction.SourceContext<Tuple4<Long, Integer, Integer, Date>> ctx) throws Exception {

        LongAdder adder = new LongAdder();
        while (isRunning) {
            adder.add(1);
            Thread.sleep(100);
            Tuple4<Long, Integer, Integer, Date> event = new Tuple4(adder.longValue(), RandomUtils.nextInt(1, 5), RandomUtils.nextInt(1, 5), new Date(System.currentTimeMillis() - RandomUtils.nextInt(0, 5) * 1000));


            log.info("event occur at = {}", event);

            ctx.collect(event);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}