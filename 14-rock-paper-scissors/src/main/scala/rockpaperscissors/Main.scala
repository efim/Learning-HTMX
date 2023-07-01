package rockpaperscissors

import mainargs.{main, arg, ParserForMethods}
import cask.main.Routes
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.Thymeleaf

import scala.jdk.CollectionConverters._

object Main {
  @main def run(
      @arg(
        name = "port",
        short = 'p',
        doc = "Port on which server will start serving"
      )
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

  def main(args: Array[String]): Unit = {
    println(s"got args : $args")
    ParserForMethods(this).runOrExit(args)
  }

  case class AppRoutes()(implicit cc: castor.Context, log: cask.Logger)
      extends cask.Routes {
    val templateResolver = new ClassLoaderTemplateResolver()
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html")
    templateResolver.setTemplateMode("HTML5")

    val templateEngine = new TemplateEngine()
    templateEngine.setTemplateResolver(templateResolver)

    @cask.get("/")
    def index(req: cask.Request) = {
      val context = new Context()
      println(s"getting request for ${req.remainingPathSegments}")
      context.setVariable(
        "myVar",
        "Hello, from Scala world!"
      )
      val result = templateEngine.process("index", context)
      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }

    @cask.get("/select/:choice")
    def acceptPlayerVote(choice: String) = {
      val context = new Context()
      val result = templateEngine.process("showdown", Set("showdown-table").asJava, context)
      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }

    @cask.staticResources("/public")
    def publicFiles(req: cask.Request) = {
      println(s"getting request for ${req.remainingPathSegments}")
      "public"
    }

    initialize()
  }

}
