package free

import cats.~>

import scala.concurrent.Future

object CountryOpsInterpreters {

  /**
    * Build a interpreter of the Free algebra out of an implementation of the tagless API
    * by simply extracting the parameters from each case class and delegating to the
    * api implementation.
    */
  def fCountryInterpreter[F[_]](t: tagless.CountriesApi[F]) =
    new (CountriesAlg ~> F) {
      override def apply[A](fa: CountriesAlg[A]): F[A] = fa match {
        case CountyDetail(country) => t.countryDetail(country)

        case Countries() => t.countries
      }
    }

  val listStateCountryInterpreter = fCountryInterpreter[ListState](tagless.CountriesStateInterpreter)

  val futureCountryInterpreter = fCountryInterpreter[Future](tagless.CountriesFutureInterpreter)

}
