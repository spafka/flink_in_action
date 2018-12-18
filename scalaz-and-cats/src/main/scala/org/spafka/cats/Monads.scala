package org.spafka.cats


object Monads {

  def main(args: Array[String]): Unit = {

    import cats.Monad
    import cats.instances.list._
    import cats.instances.option._

    val xs = List(1, 2, 3)
    val listMonad = Monad[List]

    val a = listMonad.flatMap(xs)(x ⇒ listMonad.pure(x * 2)) // 2, 4, 6
    listMonad.map(xs)(_ + 1) // 2, 3, 4

    val o = Option(1)
    val optionMonad = Monad[Option]

    optionMonad.flatMap(o)(x ⇒ optionMonad.pure(x + 2)) // Some(3)

    import cats.Monad
    import cats.syntax.flatMap._
    import cats.syntax.functor._

    import scala.language.higherKinds

    def sumSquare2[F[_] : Monad](x: F[Int], y: F[Int]): F[Int] =
      for {
        a ← x
        b ← y
      } yield a * a + b * b


    println(sumSquare2(List(1, 2), List(3, 4)))
    println(sumSquare2(Option(2), Option(1)))

    // use Id type class to make sumSquare work with non-monad
    import cats.Id

    println(sumSquare2(1: Id[Int], 2: Id[Int]))
  }
}
