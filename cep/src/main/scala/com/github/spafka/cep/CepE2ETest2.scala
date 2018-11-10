package com.github.spafka.cep

import java.util

import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


object CepE2ETest2 {

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger(RestOptions.ADDRESS.key(), 8081)
    val flink = //StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

      StreamExecutionEnvironment.getExecutionEnvironment
    flink.getConfig.setAutoWatermarkInterval(1000)


    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val b0 = new BASEDTO
    b0.seconds1970 = "1"
    b0.deviceId = "spafka"

    val g1 = new GPSDTO()
    g1.setDeviceId("spafka")
    g1.setSeconds1970("100")

    val g2 = new GPSDTO()
    g2.setDeviceId("spafka")
    g2.setSeconds1970("50")


    val g3 = new GPSDTO()
    g3.setDeviceId("spafka")
    g3.setSeconds1970("20")

    val g4 = new GPSDTO()
    g4.setDeviceId("spafka")
    g4.setSeconds1970("30")

    val g5 = new GPSDTO()
    g5.setDeviceId("spafka")
    g5.setSeconds1970("20")

    val s1 = new SensorDTO
    s1.deviceId = "spafka"
    s1.setSeconds1970("20")


    val cepStream: KeyedStream[BASEDTO, String] = flink.fromElements(b0, g1, g2, g3, g4, g5, s1)
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[BASEDTO](Time.seconds(10)) {
        override def extractTimestamp(element: BASEDTO): Long = element.seconds1970.toLong
      }
      ).keyBy(_.deviceId)

    val p1: Pattern[BASEDTO, GPSDTO] =
      Pattern.begin[BASEDTO]("start")
        .subtype(classOf[GPSDTO])
        .followedBy("sensor")
        .subtype(classOf[SensorDTO])
        .followedBy("second")
        .subtype(classOf[GPSDTO])

    CEP.pattern(cepStream, p1).select(new PatternSelectFunction[BASEDTO, Unit] {
      override def select(map: util.Map[String, util.List[BASEDTO]]): Unit = {
        System.err.println(map.get("start").get(0))
        System.err.println(map.get("sensor").get(0))
        System.err.println(map.get("second").get(0))
      }
    })


    flink.execute("cep")

  }

}
