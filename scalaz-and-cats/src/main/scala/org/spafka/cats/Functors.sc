import scala.language.higherKinds

import cats.Functor
import cats.instances.list._
import cats.instances.option._

// 通过 Functor.apply 获取 Functor 实例
val listFunctor: Functor[List] = Functor[List]
val optionFunctor: Functor[Option] = Functor[Option]

val xs = List(1, 2, 3)
listFunctor.map(xs)(_ + 1)  // 2, 3, 4

val o = Option(1)
optionFunctor.map(o)(_ + 1)  // Some(2)