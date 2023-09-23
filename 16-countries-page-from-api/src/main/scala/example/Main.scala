package example

import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import cask.model.Response
object MinimalApplication extends cask.MainRoutes{
  @cask.get("/")
  def hello() = {
    val context = new Context()
    val yo = engine.process("lala", context)
    Response(
      yo,
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

  def buildTemplateEngine(): TemplateEngine = {
    import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
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
}
