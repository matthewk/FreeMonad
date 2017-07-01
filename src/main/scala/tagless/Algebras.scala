package tagless
import model.{Country, CountryDetail, LogLevel}

trait CountriesApiAlg[F[_]] {
  def getCountries: F[List[Country]]

  def getCountyDetail(country: Country): F[CountryDetail]
}

trait Logger[F[_]] {
  def logMsg(level: LogLevel, msg: String): F[Unit]
}
