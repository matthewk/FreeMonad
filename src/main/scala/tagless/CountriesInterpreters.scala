package tagless

import model.{Country, CountryDetail}

import scala.concurrent.Future

object CountriesData {
  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )
}

object CountriesFutureInterpreter extends CountriesApi[Future] {
  import CountriesData._

  override def getCountries: Future[List[Country]] =
    Future.successful(countries.toList)

  override def getCountryDetail(country: Country): Future[Option[CountryDetail]] =
    Future.successful(countryDetail.find(_.name.equalsIgnoreCase(country.name)))
}

object CountriesStateInterpreter extends CountriesApi[ListState] {
  import CountriesData._

  override def getCountries: ListState[List[Country]] = {
    val result = countries.toList
    addToState(result.mkString(","), result)
  }

  override def getCountryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = countryDetail.find(_.name.equalsIgnoreCase(country.name))
    addToState(result.toString, result)
  }
}
