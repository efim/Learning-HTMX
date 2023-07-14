package multistepform

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import cask.endpoints.ParamReader
import java.util.UUID
import scala.jdk.CollectionConverters._
import multistepform.Models.Answers

case class Routes()(implicit cc: castor.Context, log: cask.Logger)
    extends cask.Routes {
  val templateResolver = new ClassLoaderTemplateResolver()
  templateResolver.setPrefix("templates/");
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML5")

  val templateEngine = new TemplateEngine()
  templateEngine.setTemplateResolver(templateResolver)

  val sessoinCookieName = "sessionId"

  /** This route works with and without 'sessionId' cookie present set's this
    * cookie if not present, and returns initial 'index.html' where the form is
    * not yet initialized, and will be requested for the session
    */
  @cask.get("/")
  def getIndex(ctx: cask.Request) = {
    val sessionCookie = ctx.cookies.get(sessoinCookieName)
    lazy val newSessionCookies = sessionCookie match {
      case None =>
        Seq(
          cask.Cookie(
            sessoinCookieName,
            UUID.randomUUID().toString(),
            path = "/"
          )
        )
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
    val newSessionCookie =
      cask.Cookie(sessoinCookieName, UUID.randomUUID().toString(), path = "/")
    println(s"setting new session ${newSessionCookie.value}")
    cask.Response(
      s"New session forced. Force new session",
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8"),
      cookies = Seq(newSessionCookie)
    )
  }

  val formDataContextVarName = "formData"

  /** This method only works when cookie 'sessionId' is present will get or init
    * Form State for the session, and return last unsubmitted form step fragment
    */
  @cask.get("/get-form")
  def getForm(sessionId: cask.Cookie) = {
    val id = sessionId.value
    val state = Sessions.sessionReplies.getOrElse(id, Answers(id))
    println(s"starting form for $state")
    val context = new Context()
    context.setVariable(formDataContextVarName, state)
    val formFragment = templateEngine.process(
      state.fragmentName,
      Set("formFragment").asJava,
      context
    )
    cask.Response(
      formFragment,
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
    )
  }

  // i guess let's make step a hidden input?
  @cask.post("/submit-step/:stepNum")
  def submitStep(
      sessionId: cask.Cookie,
      stepNum: Int,
      request: cask.Request
  ) = {
    val id = sessionId.value
    println(s"got $request for $id. it's data is ${request.text()}")

    val userAnswers = Sessions.sessionReplies.getOrElse(id, Answers(id))

    val updatedAnswers = userAnswers.updateStep(stepNum, request.text())

    Sessions.sessionReplies.update(id, updatedAnswers)

    val context = new Context()
    context.setVariable(formDataContextVarName, updatedAnswers)
    val nextFormFragment = templateEngine.process(
      updatedAnswers.fragmentName,
      Set("formFragment").asJava,
      context
    )
    println(s"the state now is $updatedAnswers")
    cask.Response(
      nextFormFragment,
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
    )
  }

  @cask.staticResources("/public")
  def publicFiles() = "public"

  initialize()
}
