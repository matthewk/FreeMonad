package tagless

import cats.Monad
import model.{Country, CountryDetail}
import cats.implicits._
import model.LogLevel._

import scala.language.higherKinds

class CountriesService[F[_]: Monad](countriesApi: CountriesApiAlg[F], logger: Logger[F]) {

  import logger._, countriesApi._

  def getCountriesWithDetails: F[List[(Country, CountryDetail)]] =
    for {
      _  <- logMsg(InfoLevel, "Starting")
      _  <- logMsg(InfoLevel, "Getting Countries")
      cs <- getCountries
      _  <- logMsg(InfoLevel, "Getting Details")
      cd <- cs.traverse(getCountyDetail)
      _  <- logMsg(InfoLevel, "Completed")
    } yield cs.zip(cd)
}
