package free

import model.{Country, CountryDetail}

sealed trait CountriesAlg[A]

case class GetCountries() extends CountriesAlg[List[Country]]
case class GetCountyDetail(country: Country) extends CountriesAlg[Option[CountryDetail]]

