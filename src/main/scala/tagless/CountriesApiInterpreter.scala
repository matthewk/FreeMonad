package tagless

import model.{Country, CountryDetail}

import scala.concurrent.Future

object CountriesApiInterpreter extends CountriesApiAlg[Future] {

  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )

  override def getCountries: Future[List[Country]] =
    Future.successful(countries.toList)

  override def getCountryDetail(
      country: Country): Future[Option[CountryDetail]] =
    Future.successful(countryDetail.find(_.name.equalsIgnoreCase(country.name)))
}

object StateCountriesApiInterpreter extends CountriesApiAlg[ListState] {

  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )

  override def getCountries: ListState[List[Country]] = {
    val result = countries.toList
    addToState(result.mkString(","), result)
  }

  override def getCountryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = countryDetail.find(_.name.equalsIgnoreCase(country.name))
    addToState(result.toString, result)
  }
}
