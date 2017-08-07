package free

import model.{Country, CountryDetail}
import cats.implicits._
import utils.ApplicationWrapper

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

object FreeCountryApplication extends App with ApplicationWrapper {
  import scala.concurrent.ExecutionContext.Implicits.global
  import CountriesService._

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
      val fResult = Await
        .result(FutureBasedApplication.program, atMost = Duration.Inf)

      fResult.foreach { lc =>
        printf("%-5s %-10s %-10s %-10s %-10s\n",
               "",
               lc._1.name,
               lc._1.capital,
               lc._1.region,
               lc._2.map(_.currency))
      }
    }

    appLogViewer {
      StateBasedApplication.result._1.foreach(l => println(s"""\t$l"""))
    }
  }
}
