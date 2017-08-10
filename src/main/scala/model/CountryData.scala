package model

object CountryData {
  private val england = "England"
  private val spain = "Spain"
  private val europe = "Europe"

  val countryData = List(
    Country(england, "London", europe, "flag"),
    Country(spain, "Madrid", europe, "flag")
  )

  val countryDetailData = List(
    CountryDetail(england, "GBP"),
    CountryDetail(spain, "Euro")
  )

  def printResult(c: Country, d: Option[CountryDetail]): Unit = {
    printf(
      "%-5s %-10s %-10s %-10s %-10s\n",
      "",
      c.name,
      c.capital,
      c.region,
      d.map(_.currency))
  }
}
