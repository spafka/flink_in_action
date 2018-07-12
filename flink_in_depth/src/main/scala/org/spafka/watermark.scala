package org.spafka

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.watermark.Watermark

///**
//  * This generator generates watermarks assuming that elements come out of order to a certain degree only.
//  * The latest elements for a certain timestamp t will arrive at most n milliseconds after the earliest
//  * elements for timestamp t.
//  */
//class BoundedOutOfOrdernessGenerator extends AssignerWithPeriodicWatermarks[MyEvent] {
//
//  val maxOutOfOrderness: Long = 3500L // 3.5 seconds
//
//  var currentMaxTimestamp: Long = _
//
//  override def extractTimestamp(element: MyEvent, previousElementTimestamp: Long): Long = {
//    val timestamp = element.createTime
//    currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
//    timestamp;
//  }
//
//  override def getCurrentWatermark(): Watermark = {
//    // return the watermark as current highest timestamp minus the out-of-orderness bound
//    new Watermark(currentMaxTimestamp - maxOutOfOrderness);
//  }
//}

case class MyEvent(createTime: Long)


import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


object WatermarkTest {

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("USAGE:\nSocketWatermarkTest <hostname> <port>")
      System.exit(1)
    }

    val hostName = args(0)
    val port = args(1).toInt

    val configuration = new Configuration
    configuration.setInteger("web.port", 8081)
    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val input = env.socketTextStream(hostName, port)

    input.map(f => {
      val arr = f.split("\\W")
      val code = arr(0)
      val time = arr(1).toLong
      (code, time)
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, Long)](Time.seconds(1)) {
      override def extractTimestamp(element: (String, Long)): Long = {

        println(element)
        element._2
      }
    }).keyBy(_._1)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      .process(new ProcessWindowFunction[(String, Long), String, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {


         println(s"${context.currentWatermark}, ${elements.toList}")
        }
      })

    env.execute()
  }

}