//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0

package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

object App extends cask.MainRoutes {
  @cask.get("/")
  def index() = Page.markdown

  @cask.staticFiles("/dist") // this is what path gets matched
  def distFiles() = "dist" // this is os path where files are looked up, for the generated files

  @cask.staticFiles("/public") // this is what path gets matched
  def publicFiles() = "public" // this is os path where files are looked up, for the committed files

  initialize()
}
