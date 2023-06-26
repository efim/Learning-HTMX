package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

object Page {
  val markdown = doctype("html")(
    html(
      lang := "en",
      head(
        meta(charset := "UTF-8"),
        meta(
          name := "viewport",
          content := "width=device-width, initial-scale=1.0",
        ),
        tags2.title("Frontend Mentor | Single Price Grid Component"),
        link(rel := "icon", `type` := "image/png", href := "/public/images/favicon-32x32.png" ),
        link(rel := "stylesheet", href := "/dist/output.css")
      ),
      body(
        cls := "bg-blue-100",
        h1(
          cls := "text-3xl",
          "Welcome to the future")
      )
    )
  )
}
