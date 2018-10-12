package com.github.spafka.util

import java.io.IOException
import java.net.BindException
import java.util

import akka.actor.{ActorSystem, Address, ExtendedActorSystem, Extension, ExtensionKey}
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}

/** [[akka.actor.ActorSystem]] [[Extension]] used to obtain the [[Address]] on which the
  * given ActorSystem is listening.
  *
  * @param system
  */
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
         |        hostname = localhost
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


  /**
    * Starts an Actor System at a specific port.
    *
    * @param configuration    The Flink configuration.
    * @param listeningAddress The address to listen at.
    * @param listeningPort    The port to listen at.
    * @param logger           the logger to output log information.
    * @return The ActorSystem which has been started.
    * @throws Exception
    */
  @throws[Exception]
  def startActorSystem(configuration: util.HashMap[String, String], listeningAddress: String, listeningPort: Int, logger: Logger): ActorSystem = {

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

  def main(args: Array[String]): Unit = {
    val actorSystem = startActorSystem(null, "0.0.0.0", 6132, LoggerFactory.getLogger("root"))

    val address = AkkaUtils.getAddress(actorSystem)
    print(address)
  }


}
