package example

import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import java.io.Writer
import java.io.PrintWriter
import java.io.File
import org.thymeleaf.exceptions.TemplateEngineException
import org.thymeleaf.templateresolver.TemplateResolution
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.text.SimpleDateFormat
import java.util.Date

object TestTemplate extends App {

  val now = new SimpleDateFormat("dd MMMM YYYY - HH:mm").format(new Date())
  import scala.beans.BeanProperty
  class Person(
      @BeanProperty var firstName: String,
      @BeanProperty var lastName: String
  ) {
    override def toString = s"Person: $firstName $lastName"
  }

  val ctx = new Context()
  ctx.setVariable("today", now)
  ctx.setVariable("me", new Person("Efim", "Nefedov"))

  val templateResolver = new ClassLoaderTemplateResolver()
  templateResolver.setPrefix("templates/");
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML5")

  val templateEngine = new TemplateEngine()
  templateEngine.setTemplateResolver(templateResolver)

  templateEngine.setTemplateResolver(templateResolver)

  val writer = new PrintWriter(System.out)
  val result = templateEngine.process("home", ctx)

  // VIEW RESULTS
  val text = new String(result.getBytes())
  println("TEXT: " + text)

}
