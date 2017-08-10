package tagless

import cats.implicits._
import model.{Country, CountryData, CountryDetail}
import utils.ApplicationWrapper

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FinallyCountryApplication extends App with ApplicationWrapper {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  /**
    * Bringing together the CountriesService (representing the program) and
    * some State Monad based interpreters that will provide implementation
    * for the program. The State Monad allows us to accumulate state during
    * the execution of the application and inspect that state once execution
    * has completed.
    */
  object StateBasedApplication {
    val countriesService =
      new CountriesService(
        CountriesStateInterpreter,
        LoggerStateInterpreter)

    val result: (List[String], List[(Country, Option[CountryDetail])]) =
      countriesService.getCountriesWithDetails.runEmpty.value
  }

  /**
    * Bringing together the CountriesService (representing the program) and
    * Future of Option interpreters that will provide implementations for the
    * program.
    */
  object FutureBasedApplication {
    val countriesService =
      new CountriesService(CountriesFutureInterpreter, LoggerFutureInterpreter)

    val result: Future[List[(Country, Option[CountryDetail])]] =
      countriesService.getCountriesWithDetails
  }

  application("Tagless Final") {
    appVariantExecution("State Monad") {
      StateBasedApplication.result._2.foreach {
        case (c, d) => CountryData.printResult(c, d)
      }
    }

    appVariantExecution("Future") {
      val futureResult = Await
        .result(FutureBasedApplication.result, atMost = Duration.Inf)
      futureResult.foreach {
        case (c, d) => CountryData.printResult(c, d)
      }
    }

    appLogViewer(
      StateBasedApplication.result._1.foreach(l => println(s"""\t$l""")))
  }


}
