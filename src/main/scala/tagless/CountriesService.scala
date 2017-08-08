package tagless

import cats.Monad
import cats.implicits._
import model.{Country, CountryDetail}

import scala.language.higherKinds

class CountriesService[F[_]: Monad](countriesApi: CountriesApi[F], logger: LoggerApi[F]) {

  import countriesApi._
  import logger._

  def getCountriesWithDetails: F[List[(Country, Option[CountryDetail])]] =
    for {
      _  <- info("Starting")
      _  <- info("Getting Countries")
      cs <- getCountries
      _  <- info("Getting Details")
      cd <- cs.traverse(getCountryDetail)
      _  <- info("Completed")
    } yield cs.zip(cd)
}
