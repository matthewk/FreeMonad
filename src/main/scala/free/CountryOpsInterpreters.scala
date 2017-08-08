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

  val futureCountryInterpreter = fCountryInterpreter[Future](tagless.CountriesApiInterpreter)

}
