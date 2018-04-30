package com.spaflink.flink.source

import com.spaflink.flink.bean.ComputeConf
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory
import scalaj.http.Http

import scala.util.Try


class ConfSource(confUrl: String) extends SourceFunction[ComputeConf] {
  private val LOG = LoggerFactory.getLogger(classOf[ConfSource])

  @volatile private var isRunning: Boolean = true

  override def run(sourceContext: SourceContext[ComputeConf]): Unit = {
    implicit val formats = DefaultFormats
    while (true) {
      Try {
        Http(confUrl).timeout(2000, 60000).asString
      }.toOption match {
        case Some(response) =>
          response.code match {
            case 200 => {
              parse(response.body).extractOpt[ComputeConf] match {
                case Some(conf) => {
                  LOG.info("Pulled configuration: {}", response.body)
                  sourceContext.collect(conf)
                }
                case None => LOG.warn("Invalid configuration: {}", response.body)
              }
            }
            case _ => LOG.warn("Pull configuration failed: {}", response.body)
          }
        case None => LOG.warn("Failed to invoke config API")
      }
      Thread.sleep(60000L)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }

}
