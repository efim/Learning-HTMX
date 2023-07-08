package multistepform

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

case class Routes()(implicit cc: castor.Context, log: cask.Logger)
    extends cask.Routes {
  val templateResolver = new ClassLoaderTemplateResolver()
  templateResolver.setPrefix("templates/");
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML5")

  val templateEngine = new TemplateEngine()
  templateEngine.setTemplateResolver(templateResolver)

  @cask.get("/")
  def getIndex() = {
    val context = new Context()
    val indexPage = templateEngine.process("index", context)
    cask.Response(
      indexPage,
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
    )
  }

  @cask.staticResources("/public")
  def publicFiles() = "public"

  initialize()
}
