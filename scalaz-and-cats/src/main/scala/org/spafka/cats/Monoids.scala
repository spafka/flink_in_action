package org.spafka.cats

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit m: Monoid[A]): Monoid[A] = m

  implicit val andMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = true

      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

  implicit val norMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      override def empty: Boolean = true

      override def combine(x: Boolean, y: Boolean): Boolean =
        (!x || y) && (x || !y)
    }

  // 满足结合律
  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))

  // 满足单位元法则
  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
    m.combine(x, m.empty) == x && m.combine(m.empty, x) == x

  def main(args: Array[String]): Unit = {

    println(associativeLaw(true, false, true)(andMonoid)) // true

    println(identityLaw(true)(andMonoid)) // true
    println(identityLaw(false)(andMonoid)) // true
    println(associativeLaw(true, false, true)(norMonoid))
    println(identityLaw(true)(norMonoid))
    println(identityLaw(false)(norMonoid))

    import cats.instances.int._
    import cats.instances.set._
    import cats.syntax.semigroup._
    println(Set(1, 2, 3) |+| Set(1, 2, 4))

    import cats.instances.map._
    println(Map("a" → 1) |+| Map("b" → 2))

    import cats.instances.string._
    println("Hello" |+| " world!")

    import cats.instances.int._
    1 |+| 2



    val f1: Int => Int = i => i + 1
    val f2: Int => Int = i => i + 2
    val f3 = f1 compose f2

  }
}