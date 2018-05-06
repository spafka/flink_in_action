package org.spafka.streaming.flink.watermark

import java.util.UUID

import org.joda.time.{DateTime, Interval}

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class Pageview(
                     url: String,
                     timestamp: Long,
                     eventId: String = UUID.randomUUID.toString)

object Pageview {
  val urlBase = "http://site.com/"
  val urlCount = 10

  def randomPageviews(interval: Interval, millisBetweenEvents: Long): Seq[Pageview] = {
    var timestamp = interval.getStart
    val pageviews = ListBuffer.empty[Pageview]
    while (timestamp isBefore interval.getEnd) {
      pageviews += randomPageview(timestamp)
      timestamp = timestamp plus millisBetweenEvents
    }
    pageviews
  }

  def randomPageview(timestamp: DateTime): Pageview = Pageview(randomUrl, timestamp.getMillis)

  def randomUrl = urlBase + Random.nextInt(urlCount)
}
