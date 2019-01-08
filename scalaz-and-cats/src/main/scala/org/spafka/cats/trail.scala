package org.spafka.cats

object trail {

  def factorial(n: BigInt): BigInt =
    if (n == 0) 1
    else n * factorial(n - 1)

  def main(args: Array[String]): Unit = {
    println(factorial(1000))

    val x: CallBack[Int] = y => println(y)


  }

  type CallBack[T] = Function1[T, Unit]


}
