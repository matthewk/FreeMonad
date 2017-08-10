package tagless

import model.{Country, CountryDetail}

import scala.concurrent.Future

object CountriesFutureInterpreter extends CountriesApi[Future] {
  import model.CountriesData._

  override def getCountries: Future[List[Country]] =
    Future.successful(countries)

  override def getCountryDetail(country: Country): Future[Option[CountryDetail]] =
    Future.successful(countryDetail.find(_.name.equalsIgnoreCase(country.name)))
}

object CountriesStateInterpreter extends CountriesApi[ListState] {
  import model.CountriesData._

  override def getCountries: ListState[List[Country]] = {
    addToState(countries.mkString(","), countries)
  }

  override def getCountryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = countryDetail.find(_.name.equalsIgnoreCase(country.name))
    addToState(result.toString, result)
  }
}
