package example

import cask.model.Response
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode

case class Routes(countries: List[Country])(implicit cc: castor.Context, log: cask.Logger)
    extends cask.Routes {

  def buildTemplateEngine(): TemplateEngine = {
    val templateResolver = new ClassLoaderTemplateResolver()
    templateResolver.setTemplateMode(TemplateMode.HTML)
    templateResolver.setPrefix("/templates/")
    templateResolver.setSuffix(".html")
    templateResolver.setCacheTTLMs(3600000L);

    val templateEngine = new TemplateEngine()
    templateEngine.setTemplateResolver(templateResolver)

    templateEngine
  }
  val engine: TemplateEngine = buildTemplateEngine()

  @cask.get("/")
  def hello() = {
    val context = new Context()
    val indexPage = engine.process("index", context)

    Response(
      indexPage,
      headers = Seq("Content-Type" -> "text/html; charset=utf-8")
    )
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  @cask.staticResources("public")
  def giveStaticResources() = "public"

  initialize()

}
