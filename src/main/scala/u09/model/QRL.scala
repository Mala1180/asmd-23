package u09.model

import u09.utils.Stochastics.{cumulative, draw}
import u09.utils.Stochastics

trait QRL:
  type State
  type Action
  type Reward = Double
  type Probability = Double

  given random: scala.util.Random = new scala.util.Random()

  trait Environment extends ((State, Action) => (Reward, State))

  trait MDP extends Environment:
    def transitions(s: State): Set[(Action, Probability, Reward, State)]
    override def apply(s: State, a: Action): (Reward, State) =
      draw(cumulative(transitions(s).collect:
        case (`a`, p, r, s) => (p, (r, s))
      .toList))

  type Policy = State => Action

  // Move system configuration, where "runs" can occur
  trait System:
    def environment: Environment
    def initial: State
    def terminal: State => Boolean
    def run(p: Policy): LazyList[(Action, State)]

  // an updatable, table-oriented QFunction, to optimise selection over certain actions
  trait Q extends ((State, Action) => Reward):
    def actions: Set[Action]
    def update(s: State, a: Action, v: Reward): Q
    def bestPolicy: Policy = s => actions.maxBy(this(s, _))
    def epsPolicy(f: Probability): Policy = _ match
      case _ if Stochastics.drawFiltered(_ < f) => Stochastics.uniformDraw(actions)
      case s => bestPolicy(s)
    def vFunction: State => Reward = s => actions.map(this(s, _)).max

  // The learning system, with parameters
  trait LearningProcess:
    def system: System
    def gamma: Double
    def alpha: Double
    def epsilon: Double
    def q0: Q
    def updateQ(s: State, qf: Q): (State, Q)
    def learn(episodes: Int, episodeLength: Int, qf: Q): Q