package tagless
import model.LogLevel.{DebugLevel, ErrorLevel, InfoLevel, WarnLevel}
import model.{Country, CountryDetail, LogLevel}

trait CountriesApiAlg[F[_]] {
  def getCountries: F[List[Country]]

  def getCountryDetail(country: Country): F[Option[CountryDetail]]
}

trait LoggerApiAlg[F[_]] {

   def debug(msg: String): F[Unit] = logMsg(DebugLevel, msg)

   def warn(msg: String): F[Unit] = logMsg(WarnLevel, msg)

   def info(msg: String): F[Unit] = logMsg(InfoLevel, msg)

   def error(msg: String): F[Unit] = logMsg(ErrorLevel, msg)

   def logMsg(level: LogLevel, msg: String): F[Unit]
}
