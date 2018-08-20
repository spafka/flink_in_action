import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaProducer010}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.streaming.api.scala._

object kafka {
  def main(args: Array[String]): Unit = {

    // parse input arguments
    val params = ParameterTool.fromArgs(args)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.disableSysoutLogging
    env.getConfig.setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000))
    // create a checkpoint every 5 seconds
    env.enableCheckpointing(5000)
    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)

    val p = params.getProperties
    p.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test")

    // create a Kafka streaming source consumer for Kafka 0.10.x
    val kafkaConsumer: FlinkKafkaConsumer010[String] = new FlinkKafkaConsumer010(
      params.get("input-topic", "test"),
      new SimpleStringSchema,
      params.getProperties)

    val messageStream: DataStream[String] = env
      .addSource(kafkaConsumer)
      .map(in => in)

    messageStream
      .flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(1))
      .sum(1)
      .print()

    env.execute("Kafka")
  }

}
