package org.spafka.designPattern

import scala.concurrent._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object futureTest {

  def main(args: Array[String]): Unit = {

    import ExecutionContext.Implicits.global

    val x: Future[Int] = Future {
      Thread.sleep(1000)
      1
    }

   x onComplete {
      case Success(v) => println(v)
      case Failure(e) => e.printStackTrace()
    }
    // fuck if not use this can see anything
    Thread.sleep(10000 )
  }
}
