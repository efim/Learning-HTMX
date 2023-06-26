package pricegrid

import scalatags.Text.all._
import scalatags.Text.tags2

object Page {

  /**
   * Page content - grid layout of separate elements
   * contains grid control styles
   */
  lazy val bodyMarkup = body(
    `class` := "bg-neutral-gray w-screen h-screen px-7 py-16 drop-shadow-2xl",
    `class` := "md:grid md:place-content-center",
    div(
      `class` := "grid md:grid-cols-2 md:auto-rows-min",
      `class` := "md:h-[475px] md:w-[640px]",
      joinOurCommunity(
        `class` := "md:col-span-2 rounded-t-md",
      ),
      signup(
        `class` := "md:rounded-bl-md",
      ),
      whyUs(
        `class` := "rounded-b-md md:rounded-bl-none",
      )
    ),
    footerMarkup
  )

  lazy val joinOurCommunity = div(
    `class` := " bg-white flex flex-col gap-y-4 p-6 py-7 ",
    `class` := "md:px-10 md:gap-y-0",
    h1(
      `class` := "text-lg text-primary-cyan font-semibold",
      `class` := "md:text-xl md:my-3",
      "Join our community"
    ),
    h2(
      `class` := "text-sm text-primary-yellow font-semibold",
      `class` := "md:text-base md:my-2",
      "30-day, hassle-free money back guarantee"
    ),
    p(
      `class` := "text-xs text-grayish-blue leading-loose ",
      `class` := "md:text-sm md:leading-loose md:mb-2",
      """
  Gain access to our full library of tutorials along with expert code reviews.
  Perfect for any developers who are serious about honing their skills.
"""
    )
  )

  lazy val signup = div(
    `class` := "bg-bg-subscription text-white p-6 ",
    `class` := "md:p-10",
    h2(
      `class` := "text-md font-semibold mb-4",
      "Monthly Subscription"
    ),
    div(
      `class` := "flex flex-row items-center mb-2",
      p(`class` := "text-2xl font-bold md:text-3xl", "$29"),
      p(`class` := "font-extralight text-sm ml-3", "per month")
    ),
    p(`class` := "text-sm", "Full access for less than $1 a day"),
    button(
      `class` := "w-full bg-primary-yellow rounded-lg h-12 mt-7 drop-shadow-xl",
      `class` := "md:text-md",
      "Sign Up"
    )
  )

  lazy val whyUs = div(
    `class` := "bg-bg-why-us text-white p-6 ",
    `class` := "md:p-10",
    h2(
      `class` := "font-semibold mb-3",
      "Why Us"),
    ul(
      List(
        "  Tutorials by industry experts ",
        "  Peer &amp; expert code review ",
        "  Coding exercises ",
        "  Access to our GitHub repos ",
        "  Community forum ",
        "  Flashcard decks ",
        "  New videos every week "
      ).map(linkText =>
        li(
          `class` := "text-xs pt-1 md:font-light",
          linkText)
      )
    )
  )

  lazy val wholePageMarkup = doctype("html")(
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
          href := "https://fonts.googleapis.com/css2?family=Karla:wght@400;700&display=swap",
          rel := "stylesheet",
        ),
        link(
          rel := "icon",
          `type` := "image/png",
          href := "/public/images/favicon-32x32.png"
        ),
        link(rel := "stylesheet", href := "/dist/output.css")
      ),
      bodyMarkup
    )
  )

  lazy val footerMarkup = footer(
    p(
      cls := "attribution fixed bottom-0 inset-x-0",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor. "
      ),
      "Coded by ",
      a(href := "#", "Your Name Here")
    )
  )
}
