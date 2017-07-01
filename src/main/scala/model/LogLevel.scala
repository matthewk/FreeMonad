package model

sealed trait LogLevel
object LogLevel {

  case object ErrorLevel extends LogLevel

  case object WarnLevel extends LogLevel

  case object InfoLevel extends LogLevel

  case object DebugLevel extends LogLevel

}
