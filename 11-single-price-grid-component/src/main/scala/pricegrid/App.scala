//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0
//> using dep com.lihaoyi::mainargs:0.5.0
package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2
import scalatags.Text.short
import mainargs.{main, arg, ParserForMethods}

object App {
  @main
  def run(@arg(name = "post", short = 'p', doc = "Port on which server will start serving.")
            portArg: Int = 8080,
          @arg(name = "host", doc = "Host on which server will start serving.")
            hostArg: String = "localhost") = {
    println(s"Will start server on ${hostArg}:${portArg}")

    val server = new cask.Main {
      override val allRoutes = Seq(AppRoutes())
      override def port = portArg
      override def host = hostArg
    }
    server.main(Array.empty)
  }

  def main(args: Array[String]): Unit = ParserForMethods(this).runOrExit(args)
}

case class AppRoutes()(implicit cc: castor.Context,
                           log: cask.Logger) extends cask.Routes {
  @cask.get("/")
  def index() = Page.wholePageMarkup

  @cask.staticFiles("/dist") // this is what path gets matched
  def distFiles() = "dist" // this is os path where files are looked up, for the generated files

  @cask.staticFiles("/public") // this is what path gets matched
  def publicFiles() = "public" // this is os path where files are looked up, for the committed files

  initialize()
}
