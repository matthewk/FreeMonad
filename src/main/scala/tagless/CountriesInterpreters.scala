package tagless

import model.{CountryData, Country, CountryDetail}

import scala.concurrent.Future

object CountriesFutureInterpreter extends CountriesApi[Future] {
  import CountryData._

  override def countries: Future[List[Country]] =
    Future.successful(countryData)

  override def countryDetail(country: Country): Future[Option[CountryDetail]] =
    Future.successful(countryDetailData.find(_.name.equalsIgnoreCase(country.name)))
}

object CountriesStateInterpreter extends CountriesApi[ListState] {
  import CountryData._

  override def countries: ListState[List[Country]] = {
    addToState(countryData.mkString(","), countryData)
  }

  override def countryDetail(country: Country): ListState[Option[CountryDetail]] = {
    val result = countryDetailData.find(_.name.equalsIgnoreCase(country.name))
    addToState(result.toString, result)
  }
}
