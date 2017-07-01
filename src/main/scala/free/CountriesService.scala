package free

import cats.implicits._
import model.{Country, CountryDetail}

object CountriesService {

  import DSL._

  def fetchCountries(implicit C: CountryOps[Algebra], L: LoggerOps[Algebra]):
   Service[List[(Country, CountryDetail)]] = {

    import C._
    import L._

    for {
      _         <- info("Starting")
      _         <- info("Getting Countries")
      countries <- getCountries
      _         <- info("Getting Details")
      cd        <- countries.map(getCountryDetail).sequenceU
      _         <- info("Completed")
    } yield countries.zip(cd)
  }

}
