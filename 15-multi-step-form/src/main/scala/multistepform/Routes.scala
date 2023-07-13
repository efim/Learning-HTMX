package multistepform

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import cask.endpoints.ParamReader
import java.util.UUID

case class Routes()(implicit cc: castor.Context, log: cask.Logger)
    extends cask.Routes {
  val templateResolver = new ClassLoaderTemplateResolver()
  templateResolver.setPrefix("templates/");
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML5")

  val templateEngine = new TemplateEngine()
  templateEngine.setTemplateResolver(templateResolver)

  val sessoinCookieName = "sessionId"

  @cask.get("/")
  def getIndex(ctx: cask.Request) = {
    val sessionCookie = ctx.cookies.get(sessoinCookieName)
    lazy val newSessionCookies = sessionCookie match {
      case None => Seq(cask.Cookie(sessoinCookieName, UUID.randomUUID().toString(), path = "/"))
      case Some(_) => Seq.empty // don't set new cookies
    }

    println(s"getting cookie $sessionCookie will set new? ${newSessionCookies}")

    val context = new Context()
    val indexPage = templateEngine.process("index", context)
    cask.Response(
      indexPage,
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8"),
      cookies = newSessionCookies
    )
  }

  @cask.get("/force-new-session")
  def forceNewSession() = {
    val newSessionCookie = cask.Cookie(sessoinCookieName, UUID.randomUUID().toString(), path = "/")
    println(s"setting new session ${newSessionCookie.value}")
    cask.Response(
      s"New session forced. Force new session",
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8"),
      cookies = Seq(newSessionCookie)
    )
  }

  @cask.staticResources("/public")
  def publicFiles() = "public"

  initialize()
}
