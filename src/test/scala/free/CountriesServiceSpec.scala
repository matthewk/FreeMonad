package free

import model.{Country, CountryDetail}
import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._
import free.CountriesService.fetchCountries

class CountriesServiceSpec extends FlatSpec with Matchers {

  val countries =
    List(
      Country("England" , "London", "Europe", "flag"),
      Country("Spain"   , "Madrid", "Europe", "flag")
    )

  val countryDetail =
    List(
      CountryDetail("England" , "GBP"),
      CountryDetail("Spain"   , "Euro")
    )

  behavior of "Free Monad CountriesService"

  it should "get all countries and details for each country in a combined result" in {

    val interpreters = CountryOpsTestInterpreters(countries, countryDetail) or
      LoggerOpsInterpreters.loggerListStateInterpreter

    val listState: ListState[List[(Country, CountryDetail)]] =
      fetchCountries
      .foldMap(interpreters)

    val result = listState.runEmpty.value

    println("Consolidated log from running the interpreter:")
    result._1.foreach(l => println(s"""\t\t$l"""))

    result._2 shouldBe List(
      (Country("England", "London", "Europe", "flag"),
        CountryDetail("England", "GBP")),
      (Country("Spain", "Madrid", "Europe", "flag"),
        CountryDetail("Spain", "Euro"))
    )
  }
}
