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
    val countriesDb = loadCountries()
    val server = new cask.Main {
      override def allRoutes: Seq[cask.main.Routes] = Seq(Routes(countries = countriesDb ))
      override def port: Int = portArg
      override def host: String = hostArg
    }
    server.main(Array.empty)
  }

  def loadCountries() = {
    val countries: List[Country] = upickle.default.read[List[Country]](
      scala.io.Source.fromResource("temporary/all.json").getLines().mkString,
      true
    )
    countries
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)


}
