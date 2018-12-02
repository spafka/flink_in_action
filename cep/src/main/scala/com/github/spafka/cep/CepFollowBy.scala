package com.github.spafka.cep

import java.util.concurrent.TimeUnit
import java.{lang, util}

import org.apache.flink.cep.pattern.conditions.IterativeCondition
import org.apache.flink.cep.pattern.conditions.IterativeCondition.Context
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.cep.{EventComparator, PatternSelectFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark

import scala.collection.JavaConverters._

object CepFollowBy {

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger("web.port", 8081)
    val flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    flink.setParallelism(1)

    val cepStream = flink.addSource(new SourceFunction[BASEDTO] {
      override def run(ctx: SourceFunction.SourceContext[BASEDTO]): Unit = {
        while (true) {

          val time = System.currentTimeMillis().toString

          val b0 = new BASEDTO
          b0.seconds1970 = time
          b0.deviceId = "spafka"

          val g4 = new GPSDTO()
          g4.setDeviceId("spafka")
          g4.setSeconds1970(time)

          val call = new CallDTO()
          call.setDeviceId("spafka")
          call.setSeconds1970(time)

          ctx.collect(b0)
          ctx.collect(g4)
          ctx.collect(call)



          TimeUnit.SECONDS.sleep(1)

        }
      }

      override def cancel(): Unit = {

      }
    })

      //      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[BASEDTO](Time.seconds(1)) {
      //        override def extractTimestamp(element: BASEDTO): Long = {
      //          element.getSeconds1970.toLong
      //        }
      //      })

      .assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks[BASEDTO]() {
      override def extractTimestamp(element: BASEDTO, previousTimestamp: Long): Long = element.seconds1970.toLong

      override def checkAndGetNextWatermark(lastElement: BASEDTO, extractedTimestamp: Long) = new Watermark(lastElement.seconds1970.toLong - 1000)
    })



    //    cepStream.keyBy("deviceId").window(TumblingEventTimeWindows.of(Time.seconds(1))).process(new ProcessWindowFunction[BASEDTO, Unit, Tuple, TimeWindow] {
    //      @throws[Exception]
    //      override def process(key: Tuple, context: Context, elements: Iterable[BASEDTO], out: Collector[Unit]): Unit = {
    //
    //        elements.foreach(x=>println(x))
    //
    //      }
    //    })

    val p1 = Pattern
      .begin[BASEDTO]("base")
      .followedByAny("call").where(new IterativeCondition[BASEDTO] {
      override def filter(value: BASEDTO, ctx: Context[BASEDTO]): Boolean = {

        if (value.getClass.isAssignableFrom(classOf[CallDTO])) {
          val base: lang.Iterable[BASEDTO] = ctx.getEventsForPattern("base")
          val option = base.asScala.find(x => x.getSeconds1970.equals(value.seconds1970))
          if (option.isDefined) return true
        }
        false
      }
    }).followedByAny("gps").where(new IterativeCondition[BASEDTO] {
      override def filter(value: BASEDTO, ctx: Context[BASEDTO]): Boolean = {

        if (value.getClass.isAssignableFrom(classOf[GPSDTO])) {
          val base: lang.Iterable[BASEDTO] = ctx.getEventsForPattern("call")
          val option = base.asScala.find(x => x.getSeconds1970.equals(value.seconds1970))
          if (option.isDefined) return true
        }
        false
      }
    })


    CEP.pattern(cepStream, p1, new EventComparator[BASEDTO] {
      override def compare(o1: BASEDTO, o2: BASEDTO): Int = return o1.getCompareId - o2.getCompareId
    }).select(new PatternSelectFunction[BASEDTO, Unit] {
      override def select(map: util.Map[String, util.List[BASEDTO]]): Unit = {
        System.err.println(map + "\n-----------------------------")

      }
    })

    flink.execute("cep")

  }

}
