package free

import cats.implicits._
import free.CountriesService.fetchCountries
import model.{CountryData, Country, CountryDetail}
import org.scalatest.{FlatSpec, Matchers}

class CountriesServiceSpec extends FlatSpec with Matchers {

  import CountryData._

  behavior of "Free Monad CountriesService"

  it should "get all countries and details for each country in a combined result" in {

    val interpreters = CountryOpsTestInterpreters(countries, countryDetail) or
      LoggerOpsInterpreters.loggerListStateInterpreter

    val listState: ListState[List[(Country, Option[CountryDetail])]] =
      fetchCountries
      .foldMap(interpreters)

    val (logs, results) = listState.runEmpty.value

    println("Consolidated log from running the interpreter:")
    logs.foreach(l => println(s"""\t\t$l"""))

    results shouldBe List(
      (Country("England", "London", "Europe", "flag"), Some(CountryDetail("England", "GBP"))),
      (Country("Spain", "Madrid", "Europe", "flag"), Some(CountryDetail("Spain", "Euro")))
    )
  }
}
