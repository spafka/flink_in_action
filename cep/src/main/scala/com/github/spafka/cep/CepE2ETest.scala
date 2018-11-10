package com.github.spafka.cep

import java.{lang, util}

import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.pattern.conditions.{Context, IterativeCondition}
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._


object CepE2ETest {

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger("web.port", 8081)
    val flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val b0 = new BASEDTO
    b0.seconds1970 = "3000"
    b0.deviceId = "spafka"

    val g1 = new GPSDTO()
    g1.setDeviceId("spafka")
    g1.setSeconds1970("1000")

    val g2 = new GPSDTO()
    g2.setDeviceId("spafka")
    g2.setSeconds1970("5000")


    val g3 = new GPSDTO()
    g3.setDeviceId("spafka")
    g3.setSeconds1970("2000")

    val g4 = new GPSDTO()
    g4.setDeviceId("spafka")
    g4.setSeconds1970("3000")


    val call = new CallDTO()
    call.setDeviceId("spafka")
    call.setSeconds1970("3000")

    val cepStream: KeyedStream[BASEDTO, String] = flink.fromElements(g4, call, g1, g2, g3, g4)
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[BASEDTO](Time.seconds(10)) {
        override def extractTimestamp(element: BASEDTO): Long = {
          element.getSeconds1970.toLong
        }
      }).keyBy(_.deviceId)

    val p1 = Pattern
      .begin[BASEDTO]("base")
      .followedByAny("end").where(new IterativeCondition[BASEDTO]() {
      override def filter(value: BASEDTO, ctx: Context[BASEDTO]): Boolean = {
        if (value.getClass.isAssignableFrom(classOf[GPSDTO])) {
          val base: lang.Iterable[BASEDTO] = ctx.getEventsForPattern("base")
          val option = base.asScala.find(x => x.getSeconds1970.equals(value.seconds1970))
          return option.isDefined
        }

        if (value.getClass.isAssignableFrom(classOf[CallDTO])) {
          val base: lang.Iterable[BASEDTO] = ctx.getEventsForPattern("base")
          val option = base.asScala.find(x => x.getSeconds1970.equals(value.seconds1970))
          return option.isDefined
        }
        return false;
      }
    })


    CEP.pattern(cepStream, p1).select(new PatternSelectFunction[BASEDTO, Unit] {
      override def select(map: util.Map[String, util.List[BASEDTO]]): Unit = {
        System.err.println(map.get("base"))
        System.err.println(map.get("end"))

      }
    })

    flink.execute("cep")

  }

}
