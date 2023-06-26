package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

object Page {
  lazy val markdown = doctype("html")(
    html(
      lang := "en",
      head(
        meta(charset := "UTF-8"),
        meta(
          name := "viewport",
          content := "width=device-width, initial-scale=1.0"
        ),
        tags2.title("Frontend Mentor | Single Price Grid Component"),
        link(
          rel := "icon",
          `type` := "image/png",
          href := "/public/images/favicon-32x32.png"
        ),
        link(rel := "stylesheet", href := "/dist/output.css")
      ),
      body(
        cls := "bg-blue-100",
        h1(cls := "text-3xl", "Welcome to the future"),
        """
  Join our community

  30-day, hassle-free money back guarantee

  Gain access to our full library of tutorials along with expert code reviews.
  Perfect for any developers who are serious about honing their skills.

  Monthly Subscription

  &dollar;29 per month

  Full access for less than &dollar;1 a day

  Sign Up

  Why Us

  Tutorials by industry experts
  Peer &amp; expert code review
  Coding exercises
  Access to our GitHub repos
  Community forum
  Flashcard decks
  New videos every week

""",
        footerMarkup
      )
    )
  )

  lazy val footerMarkup = footer(
    p(
      cls := "attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor. "
      ),
    "Coded by ", a(href := "#", "Your Name Here"))
  )
}
