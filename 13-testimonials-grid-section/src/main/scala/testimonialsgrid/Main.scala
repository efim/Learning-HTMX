package testimonialsgrid

import mainargs.{main, arg, ParserForMethods}
import cask.main.Routes

import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine

import scala.jdk.CollectionConverters._

object Main {
  @main def run(
      @arg(
        name = "port",
        short = 'p',
        doc = "Port on which server will start serving."
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

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)

  case class AppRoutes()(implicit cc: castor.Context, log: cask.Logger)
      extends cask.Routes {
    val templateResolver = new ClassLoaderTemplateResolver()
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html")
    templateResolver.setTemplateMode("HTML5")

    val templateEngine = new TemplateEngine()
    templateEngine.setTemplateResolver(templateResolver)

    @cask.get("/")
    def index() = {
      val context = new Context()
      context.setVariable(
        "testimonials",
        Testimonial.sameAsRequested.asJava
      )
      val result = templateEngine.process("index", context)
      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }
    @cask.get("/testimonial/:id")
    def getTestimonial(id: String, nextOrientation: Int) = {
      // println(s"got params $nextOrientation")
      val context = new Context()
      // wow, i need copy because attributes are vars and not vals,
      // didn't have to think about this in a long long time
      val foundTestimonial =
        Testimonial.sameAsRequested.find(_.id == id).get.copy()

      foundTestimonial.setNextSizeClass(nextOrientation)
      // println(
      //   s"should change size and orientation : $foundTestimonial "
      // )
      context.setVariable("selectedTestimonials", List(foundTestimonial).asJava)
      val result = templateEngine.process("testimonialSection", context)
      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }

    @cask.staticFiles("/dist")
    def distFiles() = "dist"
    @cask.staticFiles("/public")
    def publicFiles() = "public"

    initialize()
  }

}
