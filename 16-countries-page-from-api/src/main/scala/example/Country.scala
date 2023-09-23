package example

import upickle.default._

final case class Country(
  name: String,
  alpha3Code: String,
  nativeName: String = "",
  population: Int,
  region: String,
  subregion: String,
  flag: String,
  capital: String = "",
  topLevelDomain: Option[String], // maybe optional?
  currencies: List[Currency] = List.empty,
  languages: List[Language],
  borders: List[String] = List.empty,
) derives ReadWriter

final case class Currency(
  code: String,
  name: String
) derives ReadWriter

final case class Language(
  name: String,
  nativeName: String = ""
) derives ReadWriter
