package tagless

import model.{Country, CountryDetail}
import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._

class CountriesServiceSpec extends FlatSpec with Matchers {

  val countries =
    List(
      Country("England", "London", "Europe", "flag"),
      Country("Spain", "Madrid", "Europe", "flag")
    )

  val countryDetail =
    List(
      CountryDetail("England", "GBP"),
      CountryDetail("Spain", "Euro")
    )

  behavior of "Finally Tagless CountriesService"

  it should "get all countries and details for each country in a combined result" in {

    val interpreter = new CountriesApiTestInterpreter(countries, countryDetail)
    val api = new CountriesService(interpreter, LoggerTestInterpreter)

    val result = api.getCountriesWithDetails.runEmpty.value

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
