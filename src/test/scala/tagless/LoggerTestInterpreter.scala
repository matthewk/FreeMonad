package tagless
import model.LogLevel

object LoggerTestInterpreter extends LoggerApi[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}