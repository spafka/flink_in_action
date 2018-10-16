package com.github.spafka

class RemoteMessage() extends Serializable

case class RigistMessage(host:String,map:Map[String,String]) extends RemoteMessage
case class BreakMessage() extends RemoteMessage
