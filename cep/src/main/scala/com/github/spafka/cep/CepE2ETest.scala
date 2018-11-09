package com.github.spafka.cep

import java.util

import com.github.spafka.cep.{BASEDTO, GPSDTO}
import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._


object CepE2ETest {

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger("web.port", 8081)
    val flink = //StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)

      StreamExecutionEnvironment.getExecutionEnvironment
    flink.getConfig.setAutoWatermarkInterval(1000)


    flink.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val g0 = new BASEDTO
    g0.seconds1970 = "1"
    g0.deviceId = "spafka"

    val g1 = new GPSDTO()
    g1.setDeviceId("spafka")
    g1.setSeconds1970("1")

    val g2 = new GPSDTO()
    g2.setDeviceId("spafka")
    g2.setSeconds1970("5")


    val g3 = new GPSDTO()
    g3.setDeviceId("spafka")
    g3.setSeconds1970("2")

    val g4 = new GPSDTO()
    g4.setDeviceId("spafka")
    g4.setSeconds1970("3")

    val cepStream: KeyedStream[BASEDTO, String] = flink.fromElements(g0, g1, g2, g3, g4).assignAscendingTimestamps(x => x.getSeconds1970.toLong).keyBy(_.deviceId)

    val p1: Pattern[BASEDTO, GPSDTO] = Pattern.begin[BASEDTO]("start").subtype(classOf[GPSDTO]).followedBy("second").subtype(classOf[GPSDTO])

    CEP.pattern(cepStream, p1).select(new PatternSelectFunction[BASEDTO, Unit] {
      override def select(map: util.Map[String, util.List[BASEDTO]]): Unit = {
        System.err.println(map.get("start").get(0))
        System.err.println(map.get("second").get(0))
      }
    })


    flink.execute("cep")

  }

}
