//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0

package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

object App extends cask.MainRoutes {
  @cask.get("/")
  def index() = doctype("html")(
    html(
      head(
        tags2.title("Exercise 11"),
        link(rel := "stylesheet", href := "/dist/output.css")
      ),
      body(
        cls := "bg-blue-100",
        h1("Welcome to the future")
      )
    )
  )

  @cask.staticFiles("/dist") // this is what path gets matched
  def distFiles() = "dist" // this is os path where files are looked up

  initialize()
}
