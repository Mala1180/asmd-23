package pc.rl.model

object QMatrix:

  type Node = (Int, Int)

  enum Action:
    case LEFT, RIGHT, UP, DOWN
    override def toString = Map(LEFT -> "<", RIGHT -> ">", UP -> "^", DOWN -> "v")(this)

  import Action.*

  case class Facade(
                     width: Int,
                     height: Int,
                     initial: Node,
                     terminal: PartialFunction[Node, Boolean],
                     reward: PartialFunction[(Node, Action), Double],
                     jumps: PartialFunction[(Node, Action), Node],
                     gamma: Double,
                     alpha: Double,
                     epsilon: Double = 0.0,
                     v0: Double) extends QRLImpl[Node, Action]:

    def qEnvironment(): Environment = new Environment:
      override def take(s: Node, a: Action): (R, Node) =
        // applies direction, without escaping borders
        val n2: Node = (s, a) match
          case ((n1, n2), UP) => (n1, (n2 - 1) max 0)
          case ((n1, n2), DOWN) => (n1, (n2 + 1) min (height - 1))
          case ((n1, n2), LEFT) => ((n1 - 1) max 0, n2)
          case ((n1, n2), RIGHT) => ((n1 + 1) min (width - 1), n2)
          case _ => ???
        // computes rewards, and possibly a jump
        (reward.apply((s, a)),
          jumps.orElse[(Node, Action), Node](_ => n2)(s, a))

    def qFunction = QFunction(Action.values.toSet, v0, terminal)

    def qSystem = QSystem(
      environment = qEnvironment(),
      initial,
      terminal
    )

    def makeLearningInstance() = QLearning(
      qSystem, gamma, alpha, epsilon, qFunction
    )

    def show[E](v: Node => E, formatString: String): String =
      (for
        row <- 0 until width
        col <- 0 until height
      yield formatString.format(v((col, row))) + (if (col == height - 1) "\n" else "\t"))
        .mkString("")