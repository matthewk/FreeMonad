package model

object CountriesData {
  private val england = "England"
  private val spain = "Spain"
  private val europe = "Europe"

  val countries = List(
    Country(england, "London", europe, "flag"),
    Country(spain, "Madrid", europe, "flag")
  )

  val countryDetail = List(
    CountryDetail(england, "GBP"),
    CountryDetail(spain, "Euro")
  )
}
