package u09.model

import annotation.tailrec
import collection.immutable

trait QRLImpl extends QRL:

  // MDP factories
  object MDP:

    def ofFunction(f: PartialFunction[State, Set[(Action, Probability, Reward, State)]]): MDP = s =>
      f.applyOrElse(s, (x: State) => Set())

    def ofRelation(rel: Set[(State, Action, Probability, Reward, State)]): Environment = ofFunction: s =>
      rel filter (_._1 == s) map (t => (t._2, t._3, t._4, t._5))

    def ofTransitions(rel: (State, Action, Probability, Reward, State)*): Environment = ofRelation(rel.toSet)

    def ofOracle(oracle: (State, Action) => (Reward, State)): Environment = oracle(_, _)

  // Move Map-based implementation, with defaults for terminal and unexplored states
  case class QFunction(
                        override val actions: Set[Action],
                        v0: Reward = 0.0,
                        terminal: State => Boolean = (s: State) => false,
                        terminalValue: Double = 0.0) extends Q:

    val map: collection.mutable.Map[(State, Action), Reward] = collection.mutable.Map()
    override def apply(s: State, a: Action) = if (terminal(s)) terminalValue else map.getOrElse(s -> a, v0)
    override def update(s: State, a: Action, v: Double): Q = { map += ((s -> a) -> v); this }
    override def toString = map.toString


  case class QSystem(
                      override val environment: Environment,
                      override val initial: State,
                      override val terminal: State => Boolean) extends System:

    final override def run(p: Policy): LazyList[(Action, State)] =
      immutable.LazyList.iterate((initial, p(initial), initial)):
        case (_, a, s2) => val a2 = p(s2); (s2, a2, environment(s2, a2)._2)
      .tail
      .takeWhile:
        case (s1, _, _) => !terminal(s1)
      .map:
        case (_, a, s2) => (a, s2)

  case class QLearning(
    override val system: QSystem,
    override val gamma: Double,
    override val alpha: Double,
    override val epsilon: Double,
    override val q0: Q) extends LearningProcess:

    override def updateQ(s: State, qf: Q): (State, Q) =
      val a = qf.epsPolicy(epsilon)(s)
      val (r, s2) = system.environment(s, a)
      val vr = (1 - alpha) * qf(s, a) + alpha * (r + gamma * qf.vFunction(s2))
      val qf2 = qf.update(s, a, vr)
      (s2, qf2)

    @tailrec
    final override def learn(episodes: Int, length: Int, qf: Q): Q =
      @tailrec
      def runSingleEpisode(in: (State, Q), episodeLength: Int): (State, Q) =
        if episodeLength == 0 || system.terminal(in._1)
          then in
          else runSingleEpisode(updateQ(in._1, in._2), episodeLength - 1)

      episodes match
        case 0 => qf
        case e => learn(e - 1, length, runSingleEpisode((system.initial, qf), length)._2)