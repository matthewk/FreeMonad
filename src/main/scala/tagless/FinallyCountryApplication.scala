package tagless

import model.{Country, CountryDetail}
import cats.implicits._
import scala.concurrent.Await

import scala.concurrent.duration.Duration

object FinallyCountryApplication extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  println("--------------------------------------------------------")
  println("Executing Finally Tagless\n")

  val api = new CountriesService(CountriesApiInterpreter, LoggerInterpreter)

  val future: FutureOfOption[List[(Country, CountryDetail)]] =
    api.getCountriesWithDetails

  val stateApi =
    new CountriesService(StateCountriesApiInterpreter, StateLoggerInterpreter)

  val stateResult: ListState[List[(Country, CountryDetail)]] =
    stateApi.getCountriesWithDetails

  val result = stateResult.runEmpty.value

  println("\n------- as State Monad ------")
  println(s"Completed result ==> ")
  result._2.foreach{lc =>
    printf("%-5s %-10s %-10s %-10s %-10s\n","", lc._1.name, lc._1.capital, lc._1.region, lc._2.currency)
  }

  println("\n------- or as Future   ------")
  println(s"Completed result ==> ")
  val fResult = Await.result(future.value, atMost = Duration.Inf).getOrElse(List.empty)
  fResult.foreach{lc =>
    printf("%-5s %-10s %-10s %-10s %-10s\n","", lc._1.name, lc._1.capital, lc._1.region, lc._2.currency)
  }

  println("\nWould have written the following to log ==>")
  result._1.foreach(l => println(s"""\t$l"""))

  println("--------------------------------------------------------")
}
