package org.spafka.cats


sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}


// test class
final case class Person(name: String, email: String)

/**
  * Type Class Instance 即 标有 implicit 的 Type Class 实现
  */
object JsonWriterInstances {
  // type class instance 1
  implicit val stringWriter: JsonWriter[String] =
    value => JsString(value)


  // type class instance 2
  implicit val personWriter: JsonWriter[Person] =
    person => JsObject(Map("name" → JsString(person.name), "email" → JsString(person.email)))

}


object Json {
  def toJson[A](value: A)(implicit writer: JsonWriter[A]): Json = writer.write(value)
}

object typeclazz {

  def main(args: Array[String]): Unit = {
    // 导入 type class instance
    import JsonWriterInstances._

    val p = Person("songkun", "12345@qq.com");

    // 调用 type class interface
    val pJson = Json.toJson(p)

    // JsObject(Map(name -> JsString(songkun), email -> JsString(12345@qq.com)))
    println(pJson)

  }
}
