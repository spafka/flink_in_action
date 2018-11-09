package com.github.spafka

import grizzled.slf4j.Logger
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.util.Random


object Watermark {
  val logger = Logger(Watermark.getClass)

  def main(args: Array[String]): Unit = {


    val configuration = new Configuration
    configuration.setInteger(RestOptions.ADDRESS.key(), 8081)
    val flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)
    flink.getConfig.setAutoWatermarkInterval(1000)

    println(flink.getConfig.getAutoWatermarkInterval)
    flink.setMaxParallelism(8)
    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val input = flink.addSource(new SourceFunction[String] {

      @volatile var isRuning = true;

      override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
        var a: Long = 0L;
        while (isRuning) {
          a = a + 100L;
          sourceContext.collect(s"${new Random().nextInt(10)} ${a}")
          Thread.sleep(new Random().nextInt(10))
        }
      }

      override def cancel(): Unit = {
        isRuning = false;
      }
    }).setParallelism(1)
    input.map(f => {
      val arr = f.split("\\W")
      val code = arr(0)
      val time = arr(1).toLong
      (code, time)
    }).setParallelism(1).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, Long)](Time.seconds(0)) {
      override def extractTimestamp(element: (String, Long)): Long = {
        println(element)
        element._2
      }
    }).keyBy(_._1)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))//.allowedLateness(Time.seconds(10))
      .process(new ProcessWindowFunction[(String, Long), String, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {
          println(s"key=${key}, currentWatermark = ${context.currentWatermark}, ${context.window} elements= ${elements.toList}")
        }
      })
    flink.execute()
  }

}