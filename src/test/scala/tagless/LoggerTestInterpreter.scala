package tagless
import model.LogLevel

object LoggerTestInterpreter extends Logger[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}