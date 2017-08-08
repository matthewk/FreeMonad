package free

import cats.instances.future._
import cats.syntax.functor._
import cats.{Functor, ~>}

import scala.concurrent.ExecutionContext.Implicits.global

object LoggerOpsInterpreters {
  /**
    * Implement an interpreter for the Free algebra of the Logger using
    * an instance of the tagless api
    */
  def fLoggerInterpreter[F[_]](t: tagless.LoggerApi[F])(implicit fFunctor: Functor[F]) =
    new (LoggerAlg ~> F) {
      override def apply[A](fa: LoggerAlg[A]): F[A] = fa match {
        case LogMsg(level, msg) =>
          t.logMsg(level, msg).map(_.asInstanceOf[A])
      }
    }

  val loggerListStateInterpreter =
    fLoggerInterpreter(tagless.LoggerStateInterpreter)

  val futureInterpreter =
    fLoggerInterpreter(tagless.LoggerFutureInterpreter)
}
