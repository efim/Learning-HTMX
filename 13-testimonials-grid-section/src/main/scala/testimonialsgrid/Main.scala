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

      import scala.beans.BeanProperty
      final case class Testimonial(
          @BeanProperty var author: String,
          @BeanProperty var text: String,
          @BeanProperty var age: Int
      )

      class CompatTestimonial {
        @BeanProperty var author: String = _
        @BeanProperty var text: String = _
        @BeanProperty var age: Int = _

        // Auxiliary constructor
        def this(author: String, text: String, age: Int) = {
          this() // Call to the primary constructor
          this.author = author
          this.text = text
          this.age = age
        }
      }
      val yo = new CompatTestimonial("Leopold", "Miawu", 188)
      // val yo = Testimonial("Leopold", "Miawu", 188)

      // let's experiment. ugh
      class Person(
          @BeanProperty var firstName: String,
          @BeanProperty var lastName: String,
          @BeanProperty var age: Int
      ) {
        override def toString: String =
          return String.format("%s, %s, %d", firstName, lastName, age)
      }
      val p = new Person("Efim", "Nefedov", 31)
      println(p)
      // println(p.getFirstName)

      val ugh = new JTestimonial("Hell", "lala", 1234)

      context.setVariable("justString", "oh why oh why")
      context.setVariable("oneTestimonial", ugh)
      context.setVariable(
        "testimonials",
        List(
          new JTestimonial("Leopold", "Miawu", 91),
          new JTestimonial("Aragorn", "And my sword!", 55)
        ).asJava
      )
      val result = templateEngine.process("index", context)
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
