package tagless
import model.LogLevel

object LoggerTestInterpreter extends LoggerApiAlg[ListState] {
  override def logMsg(level: LogLevel, msg: String): ListState[Unit] = {
    addToState(s"$level => $msg", ())
  }
}