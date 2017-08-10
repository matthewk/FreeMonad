package free

import model.{Country, CountryDetail}

sealed trait CountriesAlg[A]

case class Countries() extends CountriesAlg[List[Country]]
case class CountyDetail(country: Country) extends CountriesAlg[Option[CountryDetail]]

