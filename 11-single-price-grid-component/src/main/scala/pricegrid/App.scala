//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0

package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

case class AppRoutes(someVal: String)(implicit cc: castor.Context,
                           log: cask.Logger) extends cask.Routes {
  println(s"> Starting server with param $someVal")

  @cask.get("/")
  def index() = Page.wholePageMarkup

  @cask.staticFiles("/dist") // this is what path gets matched
  def distFiles() = "dist" // this is os path where files are looked up, for the generated files

  @cask.staticFiles("/public") // this is what path gets matched
  def publicFiles() = "public" // this is os path where files are looked up, for the committed files

  initialize()
}
object App extends cask.Main {
  override val allRoutes = Seq(AppRoutes("hello!"))
  override def main(args: Array[String]) = {
    println(s"server starting with args: $args")
    super.main(Array.empty)
  }
}
