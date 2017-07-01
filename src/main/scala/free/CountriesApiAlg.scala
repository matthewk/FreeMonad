package free

import model.{Country, CountryDetail, LogLevel}

sealed trait CountriesApiAlg[A]

case class GetCountries() extends CountriesApiAlg[List[Country]]
case class GetCountyDetail(country: Country) extends CountriesApiAlg[CountryDetail]

