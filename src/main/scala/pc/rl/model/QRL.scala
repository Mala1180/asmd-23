package pc.rl.model

import pc.utils.Stochastics
import pc.utils.Stochastics.{cumulative, draw}

trait QRL[S, A]:

  type R = Double // Reward
  type P = Double // Probability

  given random: scala.util.Random = new scala.util.Random()

  // An Environment where one wants to learn
  trait Environment extends ((S, A) => (R, S)):
    def take(s: S, a: A): (R, S)
    override def apply(s: S, a: A) = take(s, a)

  // An MDP is the idealised implementation of an Environment
  trait MDP extends Environment:

    def transitions(s: S): Set[(A, P, R, S)]

    override def take(s: S, a: A): (R, S) =
      draw:
        cumulative:
          transitions(s).collect:
            case (`a`, p, r, s) => (p, (r, s))
          .toList

  // A strategy to act
  type Policy = (S => A)

  // A system configuration, where "runs" can occur
  trait System:
    val environment: Environment
    val initial: S
    val terminal: S => Boolean
    def run(p: Policy): LazyList[(A, S)]

  // A VFunction
  type VFunction = S => R

  // an updatable, table-oriented QFunction, to optimise selection over certain actions
  trait Q extends ((S, A) => R):
    def actions: Set[A]
    def update(s: S, a: A, v: R): Q
    def bestPolicy: Policy = s => actions.maxBy(this(s, _))

    def explorativePolicy(f: P): Policy = _ match
      case _ if Stochastics.drawFiltered(_ < f) => Stochastics.uniformDraw(actions)
      case s => bestPolicy(s)

    def vFunction: VFunction = s => actions.map(this(s, _)).max

  // The learning system, with parameters
  trait LearningProcess:
    val system: System
    val gamma: Double
    val alpha: Double
    val epsilon: Double
    val q0: Q

    def updateQ(s: S, qf: Q): (S, Q)

    def learn(episodes: Int, episodeLength: Int, qf: Q): Q