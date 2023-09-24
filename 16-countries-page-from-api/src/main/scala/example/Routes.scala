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

  @cask.get("/")
  def indexPage(region: Option[String] = None) = {
    val context = new Context()

    val regions = countries.map(_.region).distinct.sorted.asJava
    val selectedCountries = region match {
      case None                 => countries
      case Some(selectedRegion) => countries.filter(_.region == selectedRegion)
    }

    context.setVariable("regionsSet", regions)
    context.setVariable("countriesList", selectedCountries.asJava)
    context.setVariable("allCountriesList", countries.asJava)
    context.setVariable("selectedRegion", region.getOrElse(""))

    val indexPage = engine.process("index", context)
    Response(
      indexPage,
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
          .map(_.name).asJava

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
