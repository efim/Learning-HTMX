//> using dep com.lihaoyi::cask:0.9.1
//> using dep com.softwaremill.quicklens::quicklens:1.9.4
//> using dep com.lihaoyi::pprint:0.8.1
//> using dep org.scalameta::munit:1.0.0-M1

// import munit.FunSuite
package app

object Main extends cask.MainRoutes {
  @cask.get("/")
  def hello() = {
    "Hello world!"
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  initialize()

}
