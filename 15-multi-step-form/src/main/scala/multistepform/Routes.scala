package multistepform

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import cask.endpoints.ParamReader
import java.util.UUID
import scala.jdk.CollectionConverters._
import multistepform.Models.Answers
import scala.annotation.internal.requiresCapability
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource
import com.google.i18n.phonenumbers.PhoneNumberUtil

import java.net.URLDecoder
import scala.util.Try

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
    val formData = Models.FormData(state)
    context.setVariable(formDataContextVarName, formData)
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

  @cask.post("/submit-step/:stepNum/:nextStep")
  def submitStep(
      sessionId: cask.Cookie,
      stepNum: Int,
      nextStep: Int,
      request: cask.Request
  ) = {
    val id = sessionId.value
    println(s"got $request for $id. it's data is ${request.text()}")

    // note: this is nice at step #1 because not storing anything before first submission
    // but on followup steps, if data lost - new default object is created
    // and that is not nice
    val userAnswers = Sessions.sessionReplies.getOrElse(id, Answers(id))

    val submittedData = URLDecoder.decode(request.text(), "UTF-8")

    val updatedAnswers =
      userAnswers.updateStep(stepNum, submittedData, nextStep)

    Sessions.sessionReplies.update(id, updatedAnswers)

    val context = new Context()
    val formData = Models.FormData(updatedAnswers)
    context.setVariable(formDataContextVarName, formData)
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

  @cask.post("/step1/phonenumber")
  def validateStep1PhoneNumber(request: cask.Request) = {
    val submittedData = URLDecoder.decode(request.text(), "UTF-8")
    println(
      s"getting data ${request.data} or ${request.text()} or $submittedData"
    )
    val fieldValues = submittedData
      .split("&")
      .filterNot(_.isEmpty())
      .map { field =>
        println(s"looking at field $field")
        val (name, value) = field.split("=").toList match {
          case List(name, value) => name -> value
          case name :: tail      => name -> tail.headOption.getOrElse("")
          case Nil               => ???
        }
        name -> value
      }
      .toMap

    val name = fieldValues.getOrElse("name", "")
    val email = fieldValues.getOrElse("email", "")
    val phone = fieldValues.getOrElse("phone", "")

    println(s"after parsing name=$name | email=$email | phone=$phone")

    val phoneUtils = PhoneNumberUtil.getInstance()
    val phoneNum = Try(
      phoneUtils.parse(phone, CountryCodeSource.UNSPECIFIED.name())
    ).toOption
    val isPhoneValid = phoneNum.map(phoneUtils.isValidNumber(_)).getOrElse(false)

    val error = if (isPhoneValid) "" else "Please input valid phone number"

    val context = new Context()
    context.setVariable("value", phone)
    context.setVariable("error", error)
    val inputDiv =
      templateEngine.process("step1", Set("email-input").asJava, context)

    cask.Response(
      inputDiv,
      headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
    )
  }

  /** This endpoint re-renders 'plan type inputs' so that togglng monthly\yearly
    * could redraw their html
    */
  @cask.post("/step2/planTypeInputs")
  def getPlanTypeInputs(sessionId: cask.Cookie, request: cask.Request) = {
    val id = sessionId.value
    val submittedData = URLDecoder.decode(request.text(), "UTF-8")
    println(s"requesting plan type inputs for $id and $request")
    Sessions.sessionReplies.get(id) match {
      case None =>
        cask.Response(
          "Can't find answers for your session, please reload the page",
          404
        )
      case Some(answers) => {
        // here changing yearly/monthly part of state based on passed checkbox value
        // and selected plan
        // only for purposes of rendering the fragment
        // not persisting, unless next or previous buttons are pressed
        val withYearlyParamSetAndSelectedPlan =
          answers.step2.fromFormData(submittedData)
        val updatedState =
          answers.copy(step2 = withYearlyParamSetAndSelectedPlan)
        val formData = Models.FormData(updatedState)
        val context = new Context()
        context.setVariable(formDataContextVarName, formData)
        val planTypesInputsHtml = templateEngine.process(
          "step2",
          Set("planTypesInputs").asJava,
          context
        )
        println(s"updating plan type inputs for $answers")
        cask.Response(
          planTypesInputsHtml,
          headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
        )
      }
    }
  }

  @cask.staticResources("/public")
  def publicFiles() = "public"

  initialize()
}
