package tagless

import model.LogLevel

import scala.concurrent.Future

object LoggerInterpreter extends Logger[Future] {
  override def logMsg(level: LogLevel, msg: String): Future[Unit] = {
    println(s"$level => $msg")
    Future.successful(())
  }
}

object StateLoggerInterpreter extends Logger[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}

