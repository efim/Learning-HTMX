//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::mainargs:0.5.0
//> using dep org.thymeleaf:thymeleaf:3.1.1.RELEASE
package example

import mainargs.{main, arg, ParserForMethods}

object Main {
  @main def run(
      @arg(
        name = "post",
        short = 'p',
        doc = "Port on which server will start serving."
      )
      portArg: Int = 8080,
      @arg(name = "host", doc = "Host on which server will start serving.")
      hostArg: String = "localhost"
  ): Unit = {
    println(s"Will start server on ${hostArg}:${portArg}")
    val server = new cask.Main {
      override val allRoutes = Seq(AppRoutes())
      override def port = portArg
      override def host = hostArg
    }
    server.main(Array.empty)

  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)

  case class AppRoutes()(implicit cc: castor.Context, log: cask.Logger)
      extends cask.Routes {
    @cask.get("/")
    def index() = {
      import org.thymeleaf.context.Context
      import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
      import org.thymeleaf.TemplateEngine

      val templateResolver = new ClassLoaderTemplateResolver()
      templateResolver.setPrefix("templates/");
      templateResolver.setSuffix(".html")
      templateResolver.setTemplateMode("HTML5")

      val templateEngine = new TemplateEngine()
      templateEngine.setTemplateResolver(templateResolver)

      val a = 11
      val context = new Context()
      context.setVariable("name", s"Johny $a")

      val result = templateEngine.process("basic-template", context)

      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }

    @cask.staticFiles("/dist") // this is what path gets matched
    def distFiles() = "dist"

    @cask.staticFiles("/public") // this is what path gets matched
    def publicFiles() = "public"

    initialize()
  }
}
