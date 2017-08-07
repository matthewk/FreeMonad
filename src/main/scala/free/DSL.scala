package free

import cats.free.{Free, Inject}
import model._
import model.LogLevel._

/**
  * The dsl will lift the algebra into a free monad by use of smart constructors
  * the liftF or inject helper methods on the Free object
  */
object DSL {

  /**
    * CountryOps provides a set of functions replicating the types that are found
    * in the algebra. These functions help instantiate the algebraic data types.
    */
  class CountryOps[F[_]](implicit I: Inject[CountriesApiAlg, F]) {

    def getCountries: Free[F, List[Country]] =
      Free.inject[CountriesApiAlg, F](GetCountries())

    def getCountryDetail(country: Country): Free[F, Option[CountryDetail]] =
      Free.inject[CountriesApiAlg, F](GetCountyDetail(country))

  }

  class LoggerOps[F[_]](implicit L: Inject[LoggerAlg, F]) {

    def debug(msg: String): Free[F, Unit] =
      Free.inject[LoggerAlg, F](LogMsg(DebugLevel, msg))

    def warn(msg: String): Free[F, Unit] =
      Free.inject[LoggerAlg, F](LogMsg(WarnLevel, msg))

    def info(msg: String): Free[F, Unit] =
      Free.inject[LoggerAlg, F](LogMsg(InfoLevel, msg))

    def error(msg: String): Free[F, Unit] =
      Free.inject[LoggerAlg, F](LogMsg(ErrorLevel, msg))
  }

  object CountryOps {
    implicit def countryOps[F[_]](
      implicit I: Inject[CountriesApiAlg, F]): CountryOps[F] =
      new CountryOps[F]
  }

  object LoggerOps {
    implicit def loggerOps[F[_]](implicit L: Inject[LoggerAlg, F]): LoggerOps[F] =
      new LoggerOps[F]
  }

}
