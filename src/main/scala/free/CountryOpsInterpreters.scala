package free

import cats.instances.future._
import cats.syntax.functor._
import cats.{Functor, ~>}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CountryOpsInterpreters {

  /**
    * Build a interpreter of the Free algebra out of an implementation of the tagless API
    * by simply extracting the parameters from each case class and delegating to the
    * api implementation.
    */
  def fCountryInterpreter[F[_]](t: tagless.CountriesApi[F])(implicit fFunctor: Functor[F]) =
    new (CountriesAlg ~> F) {
      override def apply[A](fa: CountriesAlg[A]): F[A] = fa match {
        case GetCountyDetail(country) =>
          t.getCountryDetail(country).map(_.asInstanceOf[A])

        case GetCountries() =>
          t.getCountries.map(_.asInstanceOf[A])
      }
    }

  val listStateCountryInterpreter = fCountryInterpreter[ListState](tagless.CountriesStateInterpreter)

  val futureCountryInterpreter = fCountryInterpreter[Future](tagless.CountriesFutureInterpreter)

}
