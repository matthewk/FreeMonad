import cats.data.{OptionT, State}
import scala.concurrent.Future

package object tagless {
  type FutureOfOption[A] = OptionT[Future, A]
  type ListState[A]      = State[List[String], A]

  def addToState[A](s: String, elem: A): ListState[A] =
    State(l => (l ++ List(s), elem))

  def addManyToState[A](s: List[String], elem: A): ListState[A] =
    State(l => (l ++ s, elem))
}
