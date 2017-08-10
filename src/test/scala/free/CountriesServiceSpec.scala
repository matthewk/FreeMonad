package free

import cats.implicits._
import free.CountriesService.fetchCountries
import model.{CountriesData, Country, CountryDetail}
import org.scalatest.{FlatSpec, Matchers}

class CountriesServiceSpec extends FlatSpec with Matchers {

  import CountriesData._

  behavior of "Free Monad CountriesService"

  it should "get all countries and details for each country in a combined result" in {

    val interpreters = CountryOpsTestInterpreters(countries, countryDetail) or
      LoggerOpsInterpreters.loggerListStateInterpreter

    val listState: ListState[List[(Country, Option[CountryDetail])]] =
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
