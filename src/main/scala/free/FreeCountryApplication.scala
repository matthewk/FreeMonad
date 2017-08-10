package free

import cats.implicits._
import model.{Country, CountryData, CountryDetail}
import utils.ApplicationWrapper

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FreeCountryApplication extends App with ApplicationWrapper {

  import CountriesService._

  import scala.concurrent.ExecutionContext.Implicits.global

  /**
    * Bringing together the program (provided by CountriesService) and
    * some State Monad based interpreters that will provide implementation
    * for the program. The State Monad allows us to accumulate state during
    * the execution of the application and inspect that state once execution
    * has completed.
    */
  object StateBasedApplication {
    val interpreters =
      CountryOpsInterpreters.listStateCountryInterpreter or
        LoggerOpsInterpreters.loggerListStateInterpreter

    val program: ListState[List[(Country, Option[CountryDetail])]] =
      fetchCountries
        .foldMap(interpreters)


    val result = program.runEmpty.value
  }

  /**
    * Bringing together the program (provided by CountriesService) and
    * Future of Option interpreters that will provide implementations for the
    * program.
    */
  object FutureBasedApplication {
    val interpreters =
      CountryOpsInterpreters.futureCountryInterpreter or
        LoggerOpsInterpreters.futureInterpreter

    val program: Future[List[(Country, Option[CountryDetail])]] =
      fetchCountries.foldMap(interpreters)
  }

  application("Free") {
    appVariantExecution("State Monad") {
      StateBasedApplication.result._2.foreach {
        case (c, d) => CountryData.printResult(c, d)
      }
      appVariantExecution("Future") {
        val fResult = Await
          .result(FutureBasedApplication.program, atMost = Duration.Inf)

        fResult.foreach {
          case (c, d) => CountryData.printResult(c, d)
        }
      }
    }

    appLogViewer {
      StateBasedApplication.result._1.foreach(l => println(s"""\t$l"""))
    }
  }
}
