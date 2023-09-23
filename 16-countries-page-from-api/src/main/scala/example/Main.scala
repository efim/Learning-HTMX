package example

import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import cask.model.Response
import mainargs.{main, arg, ParserForMethods, Flag}

object MinimalApplication extends cask.Routes{

  @main
  def run(
    @arg(name="port", short='p', doc="Port on which server will start service")
      portArg: Int = 8080,
    @arg(name="host", doc="Host on which server will start serving")
      hostArg: String = "localhost"
  ) = {
    println(s"Will start server on ${hostArg}:${portArg}")
    val server = new cask.Main {
      override def allRoutes: Seq[cask.main.Routes] = Seq(Routes())
      override def port: Int = portArg
      override def host: String = hostArg
    }
    server.main(Array.empty)
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)


}
