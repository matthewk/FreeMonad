package free

import cats.~>

import scala.concurrent.Future

object LoggerOpsInterpreters {
  val loggerListStateInterpreter = new (LoggerAlg ~> ListState) {
    override def apply[A](a: LoggerAlg[A]): ListState[A] =
      addToState(chooseMessage(a), ().asInstanceOf[A])
  }

  val futureInterpreter = new (LoggerAlg ~> Future) {
    override def apply[A](fa: LoggerAlg[A]): Future[A] =
      Future.successful(().asInstanceOf[A])
  }

  def chooseMessage[A](a: LoggerAlg[A]): String = a match {
    case LogMsg(level, msg) => s"$level => $msg"
  }
}
