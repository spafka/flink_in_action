package org.spafka.streamig.flink

import org.apache.flink.streaming.api.scala._

object StreamingJob {


  def main(args: Array[String]): Unit = {


    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.readTextFile("/Users/spafka/Desktop/flink_in_action/pom.xml")
      .flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()


    // execute program
     env.execute("Flink Batch Scala API Skeleton")
  }

}
