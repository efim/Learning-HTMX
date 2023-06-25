//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.lihaoyi::scalatags:0.12.0

// import munit.FunSuite
package app

import scalatags.Text.all._
import scala.util.Random

object Main extends cask.MainRoutes {
  @cask.get("/")
  def hello() = {
    doctype("html")(
      html(
        head(
          // title("Hello testing")
        ),
        body(
          div(
            h1("This is it, the start"),
            p("Let's keep going"),
            ul(
              Range(1, 15).map(index =>
                li(a(s"article with number $index", href := s"/article/$index"))
              )
            )
          )
        )
      )
    )
  }

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
