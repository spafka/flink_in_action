package com.github.spafka.util

import java.io.IOException
import java.net.BindException

import akka.actor.{ActorSystem, Address, ExtendedActorSystem, Extension, ExtensionKey}
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.Logger

class RemoteAddressExtensionImplementation(system: ExtendedActorSystem) extends Extension {
  def address: Address = system.provider.getDefaultAddress
}

object RemoteAddressExtension extends ExtensionKey[RemoteAddressExtensionImplementation] {}


object AkkaUtils {

  def getAkkaConfig(bindAddress: String,port:Int): Config = {
    val config =
      s"""
         |akka {
         |
         |  actor {
         |    provider = "akka.remote.RemoteActorRefProvider"
         |    default-dispatcher {
         |      fork-join-executor {
         |        parallelism-factor = 1.0
         |        parallelism-min = 2
         |        parallelism-max = 4
         |      }
         |    }
         |  }
         |}
      """.stripMargin

    val hostnameConfigString =
      s"""
         |akka {
         |  remote {
         |    netty {
         |      tcp {
         |        hostname = ${bindAddress}
         |        bind-hostname = $bindAddress
         |        port = ${port}
         |        bind-port =${port}
         |      }
         |    }
         |  }
         |}
       """.stripMargin

    ConfigFactory.parseString(config + hostnameConfigString)
  }

  def createActorSystem(akkaConfig: Config): ActorSystem = {
    // Initialize slf4j as logger of Akka's Netty instead of java.util.logging (FLINK-1650)
    ActorSystem.create("flink", akkaConfig)
  }

  def getAddress(system: ActorSystem): Address = {
    RemoteAddressExtension(system).address
  }


  @throws[Exception]
  def startActorSystem(configuration: Map[String, String], listeningAddress: String, listeningPort: Int, logger: Logger): ActorSystem = {

    logger.info("Trying to start actor system at {} {}", listeningAddress, listeningPort)
    try {
      val akkaConfig = AkkaUtils.getAkkaConfig(listeningAddress, listeningPort)
      logger.debug("Using akka configuration\n {}", akkaConfig)
      val actorSystem = AkkaUtils.createActorSystem(akkaConfig)
      logger.info("Actor system started at {}", AkkaUtils.getAddress(actorSystem))
      actorSystem
    } catch {
      case t: Throwable =>
        if (t.isInstanceOf[IOException]) {
          val cause = t.getCause
          if (cause != null && t.getCause.isInstanceOf[BindException]) throw new IOException("Unable to create ActorSystem at address " + listeningAddress + " : " + cause.getMessage, t)
        }
        throw new Exception("Could not create actor system", t)
    }
  }
}
