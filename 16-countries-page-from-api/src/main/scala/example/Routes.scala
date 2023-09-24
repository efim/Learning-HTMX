package example

import cask.model.Response
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import scala.jdk.CollectionConverters._
import cask.model.Request

case class Routes(countries: List[Country])(implicit
    cc: castor.Context,
    log: cask.Logger
) extends cask.Routes {

  /** initializing thymeleaf template engine which finds and renders html
    * templates by name
    */
  def buildTemplateEngine(): TemplateEngine = {
    val templateResolver = new ClassLoaderTemplateResolver()
    templateResolver.setTemplateMode(TemplateMode.HTML)
    templateResolver.setPrefix("/templates/")
    templateResolver.setSuffix(".html")
    templateResolver.setCacheTTLMs(3600000L);

    val templateEngine = new TemplateEngine()
    templateEngine.setTemplateResolver(templateResolver)

    templateEngine
  }
  val engine: TemplateEngine = buildTemplateEngine()

  private val pageSize = 12

  @cask.get("/")
  def indexPage(region: Option[String] = None, page: Int = 0) = {
    val context = new Context()

    val regions = countries.map(_.region).distinct.sorted.asJava
    val selectedCountries = region match {
      case None                 => countries
      case Some("")             => countries
      case Some(selectedRegion) => countries.filter(_.region == selectedRegion)
    }

    val startIndex = page * pageSize
    val countriesPage =
      selectedCountries.slice(startIndex, startIndex + pageSize)
    // if current page is not full - there will be no next page
    val nextPage = if (countriesPage.length == pageSize) page + 1 else -1
    context.setVariable("nextPage", nextPage)

    context.setVariable("regionsSet", regions)
    context.setVariable("countriesList", countriesPage.asJava)
    context.setVariable("allCountriesList", countries.asJava)
    context.setVariable("selectedRegion", region.getOrElse(""))

    val indexPage = engine.process("index", context)
    Response(
      indexPage,
      headers = Seq("Content-Type" -> "text/html; charset=utf-8")
    )
  }

  /**
   * this method returns directly set of cards
   * and new anchor for loading next page of cards
   *
   * intended to be called from "next-page-anchor" with htmx
   */
  @cask.get("/countries-cards")
  def getPageOfCountriesCards(region: Option[String] = None, page: Int = 0) = {
    val context = new Context()

    val selectedCountries = region match {
      case None                 => countries
      case Some("")             => countries
      case Some(selectedRegion) => countries.filter(_.region == selectedRegion)
    }

    val startIndex = page * pageSize
    val countriesPage =
      selectedCountries.slice(startIndex, startIndex + pageSize)
    // if current page is not full - there will be no next page
    val nextPage = if (countriesPage.length == pageSize) page + 1 else -1
    context.setVariable("countriesList", countriesPage.asJava)
    context.setVariable("selectedRegion", region.getOrElse(""))
    context.setVariable("nextPage", nextPage)

    val cards = engine.process("index", Set("cards-of-countries", "infiniteScrollAnchor").asJava, context)
    Response(
      cards,
      headers = Seq("Content-Type" -> "text/html; charset=utf-8")
    )
  }

  @cask.get("/country")
  def getCountryPage(countryName: String) = {
    val context = new Context()
    countries.find(_.name == countryName) match {
      case Some(selectedCountry) =>
        context.setVariable("country", selectedCountry)
        val borderCountries = countries
          .filter(c => selectedCountry.borders.contains(c.alpha3Code))
          .map(_.name)
          .asJava

        context.setVariable("borderCountries", borderCountries)

        val countryPage = engine.process("country", context)
        Response(
          countryPage,
          headers = Seq("Content-Type" -> "text/html; charset=utf-8")
        )
      case None => Response("", 400)
    }
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  @cask.staticResources("public")
  def giveStaticResources() = "public"

  initialize()

}
