package testimonialsgrid

import mainargs.{main, arg, ParserForMethods}
import cask.main.Routes

object Main {
  @main def run(
    @arg(name = "port", short = 'p', doc = "Port on which server will start serving.")
      portArg: Int = 8080,
    @arg(name = "host", doc = "Host on which server will start serving.")
      hostArg: String = "localhost"
  ): Unit = {
    println(s"Will start server on ${hostArg}:${portArg}")
    val server = new cask.Main {
      override def allRoutes: Seq[Routes] = Seq(AppRoutes())
      override def port: Int = portArg
      override def host: String = hostArg
    }
    server.main(Array.empty)
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)

  case class AppRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes {
    @cask.get("/")
    def index() = {
      cask.Response("Hello")
    }
    @cask.staticFiles("/dist")
    def distFiles() = "dist"
    @cask.staticFiles("/public")
    def publicFiles() = "public"

    initialize()
  }

}
