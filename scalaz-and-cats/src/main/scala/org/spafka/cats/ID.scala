package org.spafka.cats

import cats.Id


object Id extends App {
  val a = "Dave": Id[String]
  println(a)
}
