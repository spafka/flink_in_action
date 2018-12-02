package com.github.spafka.flink

import java.util.Properties

import grizzled.slf4j.Logger
import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.{Configuration, JobManagerOptions}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

object ChainTest {


  def main(args: Array[String]): Unit = {

    val configuration = new Configuration
    configuration.setInteger(JobManagerOptions.ADDRESS.key(), 8081)
    val flink = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration)


    // configure Kafka consumer
    val kafkaProps = new Properties()
    kafkaProps.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProps.setProperty("group.id", "cep")

    //topicd的名字是new，schema默认使用SimpleStringSchema()即可
    val transaction = flink
      .addSource(
        new FlinkKafkaConsumer[String]("spafka", new SimpleStringSchema(), kafkaProps)
      ).flatMap(new RichFlatMapFunction[String, String] {
      private lazy val logger = Logger("kafka")
      private lazy val counter = new LongCounter()


      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        getRuntimeContext.addAccumulator("kafka", counter)
      }

      override def flatMap(value: String, out: Collector[String]): Unit = {
        counter.add(1L)
        logger.info(s"${Thread.currentThread().getId}")
      }
    })
    flink.execute("chain")
  }
}
