package tagless

import model.{Country, CountryDetail}
import cats.data.OptionT
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global

object CountriesApiInterpreter extends CountriesApiAlg[FutureOfOption] {

  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )

  override def getCountries: FutureOfOption[List[Country]] =
    OptionT.pure(countries.toList)

  override def getCountyDetail(
      country: Country): FutureOfOption[CountryDetail] =
    OptionT.pure(countryDetail.find(_.name.equalsIgnoreCase(country.name)).get)
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

  override def getCountyDetail(country: Country): ListState[CountryDetail] = {
    val result = countryDetail.find(_.name.equalsIgnoreCase(country.name)).get
    addToState(result.toString, result)
  }
}
