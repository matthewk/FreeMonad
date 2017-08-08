package tagless

import model.{Country, CountryDetail}
import cats.implicits._
import utils.ApplicationWrapper

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object FinallyCountryApplication extends App with ApplicationWrapper {
  import scala.concurrent.ExecutionContext.Implicits.global

  /**
    * Bringing together the CountriesService (representing the program) and
    * some State Monad based interpreters that will provide implementation
    * for the program. The State Monad allows us to accumulate state during
    * the execution of the application and inspect that state once execution
    * has completed.
    */
  object StateBasedApplication {
    val countriesService =
      new CountriesService(StateCountriesApiInterpreter,
                           StateLoggerInterpreter)

    val result: (List[String], List[(Country, Option[CountryDetail])]) =
      countriesService.getCountriesWithDetails.runEmpty.value

  }

  /**
    * Bringing together the ContriesService (represetnating the program) and
    * Future of Option interpreters that will provide implementations for the
    * program.
    */
  object FutureBasedApplication {
    val countriesService =
      new CountriesService(CountriesApiInterpreter, FutureLoggerInterpreter)

    val result: Future[List[(Country, Option[CountryDetail])]] =
      countriesService.getCountriesWithDetails
  }

  application("Tagless Final") {
    appVariantExecution("State Monad") {
      StateBasedApplication.result._2.foreach { lc =>
        printf("%-5s %-10s %-10s %-10s %-10s\n",
               "",
               lc._1.name,
               lc._1.capital,
               lc._1.region,
               lc._2.map(_.currency))
      }
    }

    appVariantExecution("Future") {
      val futureResult = Await
        .result(FutureBasedApplication.result, atMost = Duration.Inf)
      futureResult.foreach { lc =>
        printf("%-5s %-10s %-10s %-10s %-10s\n",
               "",
               lc._1.name,
               lc._1.capital,
               lc._1.region,
               lc._2.map(_.currency))
      }
    }

    appLogViewer(
      StateBasedApplication.result._1.foreach(l => println(s"""\t$l""")))
  }

}
