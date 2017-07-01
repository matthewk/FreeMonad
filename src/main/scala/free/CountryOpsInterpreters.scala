package free

import cats.~>
import cats.data.OptionT
import cats.instances.future._
import model.{Country, CountryDetail}

import scala.concurrent.ExecutionContext.Implicits.global

object CountryOpsInterpreters {

  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )

  val listStateCountryInterpreter = new (CountriesApiAlg ~> ListState) {
    override def apply[A](op: CountriesApiAlg[A]): ListState[A] = op match {
      case GetCountyDetail(country) => {
        val result = countryDetail.find(_.name.equalsIgnoreCase(country.name))
        addToState(
          s"Got details: \n\t${result.map(c => s"${country.name } -> ${c.currency }").mkString("\n") }",
          result.get.asInstanceOf[A]
        )
      }

      case GetCountries() =>
        addToState(
          s"Got countries: \n\t${countries.map(c => s"${c.name } with capital ${c.capital }").mkString("\n") }",
          countries.asInstanceOf[A]
        )
    }
  }

  val futureOfOptionCountryInterpreter =
    new (CountriesApiAlg ~> FutureOfOption) {
      override def apply[A](fa: CountriesApiAlg[A]): FutureOfOption[A] =
        fa match {
          case GetCountyDetail(country) =>
            OptionT.pure(countryDetail.find(_.name.equalsIgnoreCase(country.name)).get)

          case GetCountries() =>
            OptionT.pure(countries.toList)
        }
    }
}
