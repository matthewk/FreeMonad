package free

import cats.~>
import cats.data.OptionT
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global

object LoggerOpsInterpreters {
  val loggerListStateInterpreter = new (LoggerAlg ~> ListState) {
    override def apply[A](a: LoggerAlg[A]): ListState[A] =
      addToState(chooseMessage(a), ().asInstanceOf[A])
  }

  val futureOfOptionInterpreter = new (LoggerAlg ~> FutureOfOption) {
    override def apply[A](fa: LoggerAlg[A]): FutureOfOption[A] =
      OptionT.pure(().asInstanceOf[A])
  }

  def chooseMessage[A](a: LoggerAlg[A]): String = a match {
    case LogMsg(level, msg) => s"$level => $msg"
  }
}
