package tagless

import model.{CountryData, Country, CountryDetail}

import scala.concurrent.Future

object CountriesFutureInterpreter extends CountriesApi[Future] {

  override def countries: Future[List[Country]] =
    Future.successful(CountryData.countries)

  override def countryDetail(country: Country): Future[Option[CountryDetail]] =
    Future.successful(CountryData.countryDetail.find(_.name.equalsIgnoreCase(country.name)))
}

object CountriesStateInterpreter extends CountriesApi[ListState] {

  override def countries: ListState[List[Country]] = {
    addToState(CountryData.countries.mkString(","), CountryData.countries)
  }

  override def countryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = CountryData.countryDetail.find(_.name.equalsIgnoreCase(country.name))
    addToState(result.toString, result)
  }
}
