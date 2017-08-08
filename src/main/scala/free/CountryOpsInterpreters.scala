package free

import cats.instances.future._

import cats.syntax.functor._
import cats.{Functor, ~>}
import model.{Country, CountryDetail}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CountryOpsInterpreters {

  val countries = Seq(
    Country("England", "London", "Europe", "flag"),
    Country("Spain", "Madrid", "Europe", "flag")
  )

  val countryDetail = Seq(
    CountryDetail("England", "GBP"),
    CountryDetail("Spain", "Euro")
  )

  def fCountryInterpreter[F[_]](t: tagless.CountriesApiAlg[F])(implicit fFunctor: Functor[F]) =
    new (CountriesApiAlg ~> F) {
      override def apply[A](fa: CountriesApiAlg[A]): F[A] = fa match {
        case GetCountyDetail(country) =>
          t.getCountryDetail(country).map(_.asInstanceOf[A])

        case GetCountries() =>
          t.getCountries.map(_.asInstanceOf[A])
      }
    }

  val listStateCountryInterpreter = fCountryInterpreter[ListState](tagless.StateCountriesApiInterpreter)

//    new (CountriesApiAlg ~> ListState) {
//    override def apply[A](op: CountriesApiAlg[A]): ListState[A] = op match {
//      case GetCountyDetail(country) => {
//        val result = countryDetail.find(_.name.equalsIgnoreCase(country.name))
//        addToState(
//          s"Got details: \n\t${result.map(c => s"${country.name} -> ${c.currency}").mkString("\n")}",
//          result.asInstanceOf[A]
//        )
//      }
//
//      case GetCountries() =>
//        addToState(
//          s"Got countries: \n\t${countries.map(c => s"${c.name} with capital ${c.capital}").mkString("\n")}",
//          countries.toList.asInstanceOf[A]
//        )
//    }
//  }

  val futureCountryInterpreter = fCountryInterpreter[Future](tagless.CountriesApiInterpreter)

}
