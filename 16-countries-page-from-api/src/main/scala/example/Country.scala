package example

import upickle.default._
import scala.jdk.CollectionConverters._

final case class Country(
    name: Name,
    cca3: String,
    population: Int,
    region: String,
    subregion: String = "",
    flags: Flags,
    capital: List[String] = List.empty,
    tld: Option[String] = None, // maybe optional?
    currencies: Map[String, Currency] = Map.empty,
    languages: Map[String, String] = Map.empty,
    borders: List[String] = List.empty
) derives ReadWriter {
  def currenciesView = currencies.values.map(_.name).toList.asJava
  def languagesView = languages.values.toList.asJava
  def nameView = name.common
  def capitalView = capital.headOption.getOrElse("")
  def nativeName = name.nativeName.headOption.map(_._2.common).getOrElse("")
  def topLevelDomain = tld
  def alpha3Code = cca3
  def flag = flags.svg
}

final case class Name(
    common: String,
    nativeName: Map[String, Name.Native] = Map.empty
) derives ReadWriter
object Name {
  final case class Native(
      common: String
  ) derives ReadWriter
}

final case class Currency(
    name: String,
    symbol: String = ""
) derives ReadWriter

final case class Flags(
    png: String,
    svg: String,
    alt: String = ""
) derives ReadWriter
