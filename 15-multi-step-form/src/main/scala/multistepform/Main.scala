package multistepform

import mainargs.{main, arg, ParserForMethods}

object Main {
  @main def run(
    @arg(name="port", short='p', doc="Port on which server will start service")
      portArg: Int = 8080,
    @arg(name="host", doc="Host on which server will start serving")
      hostArg: String = "localhost"
  ): Unit = {
    println(s"Will start server on ${hostArg}:${portArg}")
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)
}
