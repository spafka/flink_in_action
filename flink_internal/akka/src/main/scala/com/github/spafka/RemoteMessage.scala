package com.github.spafka

sealed class Message()

// 注册信息相关
case class RigistMessage(host: String, map: Map[String, String]) extends Message

case class BreakMessage() extends Message

// TASK 相关
// fixme

abstract class TaskDesc() extends Serializable {

  def run = {

  }
}

case class Task(tdd: TaskDesc) {

  def run = {


    val runnable: Runnable = new Runnable {
      override def run(): Unit = {
        tdd.run
      }
    }
    var invoke: Thread = new Thread(runnable)

    invoke.start()
  }

}


