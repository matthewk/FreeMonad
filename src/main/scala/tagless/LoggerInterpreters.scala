package tagless

import model.LogLevel

import scala.concurrent.Future

object LoggerFutureInterpreter extends LoggerApi[Future] {
  override def logMsg(level: LogLevel, msg: String): Future[Unit] = {
    println(s"$level => $msg")
    Future.successful(())
  }
}

object LoggerStateInterpreter extends LoggerApi[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}

