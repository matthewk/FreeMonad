package free

import cats.free.{Free, Inject}
import model.LogLevel._
import model._

/**
  * The dsl will lift the algebra into a free monad by use of smart constructors
  * the liftF or inject helper methods on the Free object
  */
object DSL {

  /**
    * CountryOps provides a set of functions replicating the types that are found
    * in the algebra. These functions help instantiate the algebraic data types.
    *
    * Note that the methods on CountryOps are the same as the methods on the finally tagless
    * CountriesApi trait. This means CountryOps can be an implementation of the tagless
    * api. So an application that is using tagless can use this class as an interpreter to
    * convert a program to the Free algebra.
    *
    * Compare that to `CountryOpsInterpreters.fCountryInterpreter`, which can interpret
    * the Free case-class algebra using an implementation of the tagless api. I.e.
    * `fCountryInterpreter` is a general way of building a Free interpreter out of
    * a tagless implementation.
    *
    * The combination of these two all trivial conversion between Free and Finally Tagless.
    */
  class CountryOps[F[_]](implicit I: Inject[CountriesAlg, F])
    extends tagless.CountriesApi[Free[F, ?]] {

    def countries: Free[F, List[Country]] =
      Free.inject[CountriesAlg, F](Countries())

    def countryDetail(country: Country): Free[F, Option[CountryDetail]] =
      Free.inject[CountriesAlg, F](CountyDetail(country))
  }

  class LoggerOps[F[_]](implicit L: Inject[LoggerAlg, F])
    extends tagless.LoggerApi[Free[F, ?]] {

    override def logMsg(level: LogLevel, msg: String): Free[F, Unit] =
      Free.inject[LoggerAlg, F](LogMsg(ErrorLevel, msg))
  }

  object CountryOps {
    implicit def countryOps[F[_]](
      implicit I: Inject[CountriesAlg, F]
    ): CountryOps[F] =
      new CountryOps[F]
  }

  object LoggerOps {
    implicit def loggerOps[F[_]](implicit L: Inject[LoggerAlg, F]): LoggerOps[F] =
      new LoggerOps[F]
  }

}
