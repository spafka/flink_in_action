package org.spafka

import org.apache.commons.lang3.RandomUtils
import org.apache.flink.configuration.Configuration
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory


object WatermarkTest2 {

  val logger = LoggerFactory.getLogger("watermarktest")

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger("web.port", 8081)
    val env = //StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

      StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.setAutoWatermarkInterval(1000)
    //env.setStateBackend(new RocksDBStateBackend(""))
    env.enableCheckpointing(10000)
    env.setMaxParallelism(4)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //val input = env.socketTextStream(args(0), args(1).toInt)

    val input = env.addSource(new SourceFunction[String] {

      @volatile var isRuning = true;

      override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
        var a: Long = System.currentTimeMillis();
        while (isRuning) {
          a = a + 100L;
          sourceContext.collect(s"${RandomUtils.nextLong(1, 4).toString} ${a}")
          Thread.sleep(100L)
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
    }).setParallelism(1)
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, Long)](Time.seconds(0)) {
        override def extractTimestamp(element: (String, Long)): Long = {
          element._2
        }
      })
      .keyBy(_._1)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(1)))
      .process(new ProcessWindowFunction[(String, Long), String, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {
          logger.info(s"key=${key}, currentWatermark = ${context.currentWatermark}, elements= ${elements.map(x => x._1).toList}")
        }
      })
    env.execute()
  }

}