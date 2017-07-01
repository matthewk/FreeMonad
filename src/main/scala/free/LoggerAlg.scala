package free

import model.LogLevel

sealed trait LoggerAlg[A]

final case class LogMsg(level: LogLevel, msg: String) extends LoggerAlg[Unit]

