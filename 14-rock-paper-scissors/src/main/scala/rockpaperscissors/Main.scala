package rockpaperscissors

import mainargs.{main, arg, ParserForMethods}
import cask.main.Routes
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.Thymeleaf

import scala.jdk.CollectionConverters._
import javax.swing.text.Position
import rockpaperscissors.Models.Positioning
import scala.util.Random
import rockpaperscissors.Models.ShowdownState

object Main {
  @main def run(
      @arg(
        name = "port",
        short = 'p',
        doc = "Port on which server will start serving"
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

  def main(args: Array[String]): Unit = {
    println(s"got args : $args")
    ParserForMethods(this).runOrExit(args)
  }

  case class AppRoutes(pathPrefix: String = "")(implicit cc: castor.Context, log: cask.Logger)
      extends cask.Routes {
    val templateResolver = new ClassLoaderTemplateResolver()
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html")
    templateResolver.setTemplateMode("HTML5")

    val templateEngine = new TemplateEngine()
    templateEngine.setTemplateResolver(templateResolver)

    @cask.get(s"$pathPrefix/")
    def index(req: cask.Request) = {
      val context = new Context()
      val choices = Models.choiceSelectionItems.asJava
      context.setVariable(
        "choiceBadges",
        choices
      )
      val result = templateEngine.process("index", context)
      cask.Response(
        result,
        headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
      )
    }

    @cask.get(s"$pathPrefix/select/:choice")
    def acceptPlayerVote(choice: String) = {
      val context = new Context()
      val badge = Models.choiceSelectionItems.find(_.c.name == choice)
      val response = badge match {
        case Some(playersChoiceBadge) =>
          val badge = playersChoiceBadge.copy()
          badge.p = Positioning.Relative
          val showdownState = ShowdownState(badge, None, false)
          context.setVariable("showdownState", showdownState)
          val result = templateEngine.process(
            "showdown",
            Set("showdown-table").asJava,
            context
          )
          cask.Response(
            result,
            headers = Seq("Content-Type" -> "text/html;charset=UTF-8")
          )
        case None =>
          cask.Response(s"Unknown choice: '${choice}'", 400)
      }
      response
    }

    @cask.get(s"$pathPrefix/house-choice/:playersChoice")
    def requestHouseChoice(playersChoice: String) = {
      val context = new Context()
      val badge = Models.choiceSelectionItems.find(_.c.name == playersChoice)
      val response = badge match {
        case Some(playersChoiceBadge) =>
          val badge = playersChoiceBadge.copy()
          badge.p =
            Positioning.Relative // this probably should be set in enclosing html tag
          val houseChoice =
            Models.choiceSelectionItems(Random.nextInt(3)).copy()
          houseChoice.p = Positioning.Relative
          println(s"getting house choice $houseChoice")
          val showdownState = ShowdownState(badge, Some(houseChoice), false)
          context.setVariable("showdownState", showdownState)
          val result = templateEngine.process(
            "showdown",
            Set("showdown-table").asJava,
            context
          )
          cask.Response(
            result,
            headers = Seq(
              "Content-Type" -> "text/html;charset=UTF-8",
              "HX-Trigger-After-Settle" -> s"""{"updateScore": ${showdownState.scoreChange}}"""
            )
          )
        case None =>
          cask.Response(s"Unknown choice: '${playersChoice}'", 400)
      }
      response
    }

    @cask.staticResources(s"$pathPrefix/public")
    def publicFiles(req: cask.Request) = {
      "public"
    }

    initialize()
  }

}
