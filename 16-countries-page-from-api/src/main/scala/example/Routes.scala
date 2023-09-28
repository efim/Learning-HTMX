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
  def indexPage(
      region: Option[String] = None,
      page: Int = 0,
      countryName: Option[String] = None
  ) = {
    val context = new Context()

    val regions = countries.map(_.region).distinct.sorted.asJava
    val selectedCountries = getSelectedCountries(region, countryName)

    val startIndex = page * pageSize
    val countriesPage =
      selectedCountries.slice(startIndex, startIndex + pageSize)
    // if current page is not full - there will be no next page
    val nextPage = if (countriesPage.length == pageSize) page + 1 else -1
    context.setVariable("nextPage", nextPage)

    context.setVariable("regionsSet", regions)
    context.setVariable("countriesList", countriesPage.asJava)
    context.setVariable("allCountriesList", countries.asJava)
    // for anchor to request next pages from same countries
    context.setVariable("selectedRegion", region.getOrElse(""))
    context.setVariable("countryName", countryName.getOrElse(""))

    val indexPage = engine.process("index", context)
    Response(
      indexPage,
      headers = Seq("Content-Type" -> "text/html; charset=utf-8")
    )
  }

  /** this method returns directly set of cards and new anchor for loading next
    * page of cards
    *
    * intended to be called from "next-page-anchor" with htmx
    */
  @cask.get("/countries-cards")
  def getPageOfCountriesCards(
      region: Option[String] = None,
      page: Int = 0,
      countryName: Option[String] = None
  ) = {
    val context = new Context()

    val selectedCountries = getSelectedCountries(region, countryName)

    val startIndex = page * pageSize
    val countriesPage =
      selectedCountries.slice(startIndex, startIndex + pageSize)
    context.setVariable("countriesList", countriesPage.asJava)

    // for anchor to request next pages from same countries
    context.setVariable("selectedRegion", region.getOrElse(""))
    context.setVariable("countryName", countryName.getOrElse(""))

    // if current page is not full - there will be no next page
    val nextPage = if (countriesPage.length == pageSize) page + 1 else -1
    context.setVariable("nextPage", nextPage)

    val cards = engine.process(
      "index",
      Set("cards-of-countries", "infiniteScrollAnchor").asJava,
      context
    )
    // this is to store switch to another region in the history
    val newUrl = s"/?region=${region.getOrElse("")}"
    // only save url when new region is requested, not on addtional card loads
    val urlHeaderOpt = if (page == 0) Seq("HX-Push" -> newUrl) else Seq.empty
    Response(
      cards,
      headers = Seq(
        "Content-Type" -> "text/html; charset=utf-8"
      ) ++ urlHeaderOpt
    )
  }

  private def getSelectedCountries(
      region: Option[String] = None,
      countryName: Option[String] = None
  ) = {
    val countriesFromSelectedRegion = region match {
      case None                 => countries
      case Some("")             => countries
      case Some(selectedRegion) => countries.filter(_.region == selectedRegion)
    }
    // applying country name filtering, can be partial name
    val selectedCountries = countryName match {
      case None     => countriesFromSelectedRegion
      case Some("") => countriesFromSelectedRegion
      case Some(partialSearchString) =>
        val lowerSearch = partialSearchString.toLowerCase()
        countriesFromSelectedRegion
          .filter(country => {
            val nameContainsPartialMatch =
              country.name.common.toLowerCase().contains(lowerSearch) ||
                country.name.nativeName.values.foldLeft(false) {
                  (acc, nativeName) =>
                    acc || nativeName.common.toLowerCase().contains(lowerSearch)
                }

            nameContainsPartialMatch
          })
    }
    selectedCountries
  }

  @cask.get("/country")
  def getCountryPage(countryName: String) = {
    val context = new Context()
    countries.find(_.name.common == countryName) match {
      case Some(selectedCountry) =>
        context.setVariable("country", selectedCountry)
        val borderCountries = countries
          .filter(c => selectedCountry.borders.contains(c.alpha3Code))
          .map(_.name.common)
          .sortBy(_.length())
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
