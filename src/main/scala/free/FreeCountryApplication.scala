package free

import model.{Country, CountryDetail}
import cats.implicits._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object FreeCountryApplication extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import CountriesService._

  println("--------------------------------------------------------")
  println("Executing Free\n")

  val interpreters =
    CountryOpsInterpreters.listStateCountryInterpreter or
      LoggerOpsInterpreters.loggerListStateInterpreter

  val listState: ListState[List[(Country, CountryDetail)]] =
    fetchCountries
    .foldMap(interpreters)

  val result = listState.runEmpty.value

  val futureInterpreters =
    CountryOpsInterpreters.futureOfOptionCountryInterpreter or
      LoggerOpsInterpreters.futureOfOptionInterpreter

  val futureProg: FutureOfOption[List[(Country, CountryDetail)]] = fetchCountries.foldMap(futureInterpreters)

  println("\n------- as State Monad ------")
  println(s"Completed result ==> ")
  result._2.foreach{lc =>
    printf("%-5s %-10s %-10s %-10s %-10s\n","", lc._1.name, lc._1.capital, lc._1.region, lc._2.currency)
  }

  println("\n------- or as Future   ------")
  println(s"Completed result ==> ")
  val fResult = Await.result(futureProg.value, atMost = Duration.Inf).getOrElse(List.empty)
  fResult.foreach{lc =>
    printf("%-5s %-10s %-10s %-10s %-10s\n","", lc._1.name, lc._1.capital, lc._1.region, lc._2.currency)
  }

  println("\nWould have written the following to log ==>")
  result._1.foreach(l => println(s"""\t$l"""))

}
