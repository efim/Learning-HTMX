//> using dep com.lihaoyi::pprint:0.8.1

object Hello {
  def main(args: Array[String]): Unit = {
    case class Thingy(a: Long, b: List[String])
    val th = Thingy(1234L, List("hello", "worlds"))
    pprint.pprintln(s"hello, i guess '${OtherObject.yoyo}'!!!")
    pprint.pprintln(th)
    println(th)

  }
}
