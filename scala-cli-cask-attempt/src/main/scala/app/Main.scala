//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0

// import munit.FunSuite
package app

import scalatags.Text.all._
import scala.util.Random
import scalatags.Text.tags2

object Main extends cask.MainRoutes {
  @cask.get("/")
  def hello() = {
    doctype("html")(
      html(
        head(
          tags2.title("Trying out the starter page"),
          meta(
            charset := "UTF-8"
          ),
          meta(
            name := "viewport",
            content := "width=device-width, initial-scale=1.0"
          ),
          link(rel := "stylesheet", href := "/dist/output.css")
          // title("Hello testing")
        ),
        body(
          cls := "bg-blue-50 grid place-content-center h-screen",
          div(
            h1(
              cls := "text-2xl font-bold",
              "This is it, the start"),
            p("Let's keep going"),
            ul(
              Range(1, 15).map(index =>
                li(a(s"article with number $index", href := s"/article/$index", cls := "underline text-blue-500"))
              )
            )
          )
        )
      )
    )
  }

  @cask.staticFiles("/dist")
  def staticFileRoutes() = "/dist"

  @cask.get("/article/:id")
  def articlePage(id: Long) = {
    doctype("html")(
      html(
        body(
          h1(s"Reading article $id"),
          p(Random.nextString(100))
        )
      )
    )
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  initialize()

}
