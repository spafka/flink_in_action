package com.github.spafka

import java.net.InetAddress

import akka.actor.{Actor, Props}
import com.github.spafka.util.AkkaUtils
import grizzled.slf4j.Logger

class Master(protected val map: Map[String, String]) extends Actor {


  // fixme add more execute
  val logger = Logger(classOf[Master])

  override def receive: Receive = {
    case e: RigistMessage => {
      print(e)
    }

    case t:Task=>{
      t.run
    }
  }

  override def preStart(): Unit = {
    super.preStart();
    logger.info(s"Start Master on ${InetAddress.getLocalHost().getHostName} on Port ${map.getOrElse("port", "6123")}")


  }
}


object Master {
  def main(args: Array[String]): Unit = {

    val LOG = Logger(classOf[Master])

    val conf = Map("port" -> "6123")
    val actorSystem = AkkaUtils
      .startActorSystem(conf,
        InetAddress.getLocalHost().getHostName,
        conf.getOrElse("port", "6123").toInt,
        LOG.logger)

    val masterActor = actorSystem.actorOf(Props(classOf[Master], conf), "flink")

    println(masterActor.path)

  }
}