package org.spafka.cats

object Functors {


  import scala.language.higherKinds

  import cats.Functor
  import cats.syntax.functor._

  def doMath[F[_]](x: F[Int])(implicit functor: Functor[F]): F[Int] =
    x.map(x ⇒ x * 2)

  def main(args: Array[String]): Unit = {

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    import scala.concurrent.{Await, Future}

    val f: Future[String] =
      Future(110)
        .map(_ + 1)
        .map(_ * 6)
        .map(_ + "!")

    println(Await.result(f, 1.second)) // 666!


    import cats.instances.list._

    val xs = List(1, 2, 3)

    doMath(xs)  // 2, 4, 6

    import cats.instances.option._

    val o = Option(1)

    doMath(o)  // Some(2)



  }
}
