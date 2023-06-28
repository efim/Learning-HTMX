package testimonialsgrid

import mainargs.{main, arg, ParserForMethods}

object Main {
  @main def run(
    @arg(name = "port", short = 'p', doc = "Port on which server will start serving.")
      portArg: Int = 8080,
    @arg(name = "host", doc = "Host on which server will start serving.")
      hostArg: String = "localhost"
  ): Unit = {
    val a = 1
    println(s"Will start server on ${hostArg}:${portArg}")
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)

}
