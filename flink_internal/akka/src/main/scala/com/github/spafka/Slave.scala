package com.github.spafka

import java.net.InetAddress

import akka.actor.Actor
import com.github.spafka.util.AkkaUtils
import grizzled.slf4j.Logger


class Slave extends Actor {

  // fixme add more executor
  override def receive: Receive = {

    case e: RigistMessage => {
      print(e)
    }
  }
}


object Slave{
  def main(args: Array[String]): Unit = {


    val LOG = Logger(classOf[Master])

    val conf = Map("port" -> "6124")
    val actorSystem = AkkaUtils
      .startActorSystem(conf,
        InetAddress.getLocalHost().getHostName,
        conf.getOrElse("port", "6123").toInt,
        LOG.logger)

    val actorSelection = actorSystem.actorSelection("akka.tcp://flink@CHINA-20180921R:6123/user/flink")

    actorSelection ! RigistMessage("localhost",null)

    actorSelection ! Task(tdd = new TaskDesc {

      override def run: Unit = {
        while (true) {
          println("task invokeing!!!! ")
        }
      }
    })

  }
}