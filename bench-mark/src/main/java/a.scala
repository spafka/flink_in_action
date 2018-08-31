package org.spafka

import org.joda.time.DateTime

object Timer {

  def time[T](block: => T): T = {
    val start = System.currentTimeMillis()
    println("\n\n--------------------------------")
    println(s"start at  ${new DateTime()}")
    val result = block // call-by-name
    val end = System.currentTimeMillis()
    println(s"end at ${new DateTime()}")
    println("past:[" + (end - start) + "ms]")
    println("--------------------------------")
    result
  }

}
