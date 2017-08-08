import cats.data.{Coproduct, OptionT, State}
import cats.free.Free

import scala.concurrent.Future

package object free {
  type FutureOfOption[A] = OptionT[Future, A]
  type Algebra[A]        = Coproduct[CountriesAlg, LoggerAlg, A]
  type Service[A]        = Free[Algebra, A]
  type ListState[A]      = State[List[String], A]

  def addToState[A](s: String, elem: A): ListState[A] =
    State(l => (l ++ List(s), elem))

  def addManyToState[A](s: List[String], elem: A): ListState[A] =
    State(l => (l ++ s, elem))
}
