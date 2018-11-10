//package com.github.spafka;
//
//import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
//import org.apache.flink.streaming.api.watermark.Watermark;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.joda.time.DateTime;
//
///**
// * This is a {@link AssignerWithPeriodicWatermarks} used to emit Watermarks that lag behind the element with
// * the maximum timestamp (in event time) seen so far by a fixed amount of time, <code>t_late</code>. This can
// * help reduce the number of elements that are ignored due to lateness when computing the final result for a
// * given window, in the case where we know that elements arrive no later than <code>t_late</code> units of time
// * after the watermark that signals that the system event-time has advanced past their (event-time) timestamp.
// */
//public abstract class BoundedOutOfOrdernessTimestampExtractor<T> implements AssignerWithPeriodicWatermarks<T> {
//
//    private static final long serialVersionUID = 1L;
//    /**
//     * The (fixed) interval between the maximum seen timestamp seen in the records
//     * and that of the watermark to be emitted.
//     */
//    public final long maxOutOfOrderness;
//    /**
//     * The current maximum timestamp seen so far.
//     */
//    public long currentMaxTimestamp;
//    /**
//     * The timestamp of the last emitted watermark.
//     */
//    public long lastEmittedWatermark = Long.MIN_VALUE;
//
//    public BoundedOutOfOrdernessTimestampExtractor(Time maxOutOfOrderness) {
//        if (maxOutOfOrderness.toMilliseconds() < 0) {
//            throw new RuntimeException("Tried to set the maximum allowed " +
//                    "lateness to " + maxOutOfOrderness + ". This parameter cannot be negative.");
//        }
//        this.maxOutOfOrderness = maxOutOfOrderness.toMilliseconds();
//        this.currentMaxTimestamp = Long.MIN_VALUE + this.maxOutOfOrderness;
//    }
//
//    public long getMaxOutOfOrdernessInMillis() {
//        return maxOutOfOrderness;
//    }
//
//    /**
//     * Extracts the timestamp from the given element.
//     *
//     * @param element The element that the timestamp is extracted from.
//     * @return The new timestamp.
//     */
//    public abstract long extractTimestamp(T element);
//
//    @Override
//    public final Watermark getCurrentWatermark() {
//        // this guarantees that the watermark never goes backwards.
//        long potentialWM = currentMaxTimestamp - maxOutOfOrderness;
//        if (potentialWM >= lastEmittedWatermark) {
//            System.err.println(String.format("currentTime=%s,currentMaxTimestamp=%s,lastEmittedWatermark=%s,potentialWM=%s",new DateTime().toString("HH:mm:ss.SSS"),currentMaxTimestamp,lastEmittedWatermark,potentialWM));
//            lastEmittedWatermark = potentialWM;
//        }
//        return new Watermark(lastEmittedWatermark);
//    }
//
//    @Override
//    public final long extractTimestamp(T element, long previousElementTimestamp) {
//        long timestamp = extractTimestamp(element);
//        if (timestamp > currentMaxTimestamp) {
//            currentMaxTimestamp = timestamp;
//        }
//        return timestamp;
//    }
//}
