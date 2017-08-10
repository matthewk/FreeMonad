package free

import cats.~>

object LoggerOpsInterpreters {
  /**
    * Implement an interpreter for the Free algebra of the Logger using
    * an instance of the tagless api
    */
  def fLoggerInterpreter[F[_]](t: tagless.LoggerApi[F]) =
    new (LoggerAlg ~> F) {
      override def apply[A](fa: LoggerAlg[A]): F[A] = fa match {
        case LogMsg(level, msg) => t.logMsg(level, msg)
      }
    }

  val loggerListStateInterpreter =
    fLoggerInterpreter(tagless.LoggerStateInterpreter)

  val futureInterpreter =
    fLoggerInterpreter(tagless.LoggerFutureInterpreter)
}
