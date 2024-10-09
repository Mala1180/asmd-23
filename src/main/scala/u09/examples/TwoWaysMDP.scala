package u09.examples

import u09.model.QRLImpl

object TwoWaysMDP:
  case class Pos(n: Int)

  enum Move:
    case LEFT, RIGHT

  val target = -5
  val reward = 10
  val penalty = -0.2
  val maxRight = 10

  val terminalTW: Pos => Boolean =
    case Pos(n) => n <= target | n >= maxRight

  object rl extends QRLImpl:
    type State = Pos
    type Action = Move

    def mdpTW(): Environment = MDP.ofFunction:
      case Pos(n) => Set(
        (Move.LEFT, 1, if n == target + 1 then reward else penalty, Pos(n - 1)),
        (Move.RIGHT, 1, penalty, if n == maxRight then Pos(n) else Pos(n + 1)))

    def qfTW() = QFunction(Move.values.toSet, 1.0, terminalTW)
    def rlTW() = QLearning(
      system = QSystem(mdpTW(), Pos(0), terminalTW),
      gamma = 0.9,
      alpha = 0.5,
      epsilon = 0.0,
      q0 = qfTW()
    )
