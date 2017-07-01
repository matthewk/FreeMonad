package tagless

import cats.data.OptionT
import cats.instances.future._
import model.LogLevel

import scala.concurrent.ExecutionContext.Implicits.global

object LoggerInterpreter extends Logger[FutureOfOption] {
  override def logMsg(level: LogLevel, msg: String): FutureOfOption[Unit] = {
    println(s"$level => $msg")
    OptionT.pure(())
  }
}

object StateLoggerInterpreter extends Logger[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}

