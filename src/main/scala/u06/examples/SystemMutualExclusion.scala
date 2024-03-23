package pc.examples

import pc.modelling.System

object SystemMutualExclusion:

  enum State:
    case N,T,C

  export State.*
  export pc.modelling.SystemAnalysis.*

  type States = List[State]

  // helper
  private def move(l: States)(from: State, to: State): Set[States] =
    (0 until l.size).toSet.collect:
      case i if l(i) == from => l.updated(i, to)

  // System specification, try to capture the abstraction a bit
  def mutualExclusion: System[States] = l =>
    move(l)(N,T) ++ move(l)(C,N) ++ (if (l.contains(C)) Set() else move(l)(T,C))

@main def mainSystemMutualExclusion() =
  import SystemMutualExclusion.*
  println(mutualExclusion.next(List(N,N,N)))
  println(mutualExclusion.next(List(N,T,T)))
  println(mutualExclusion.next(List(N,T,C)))

  println(mutualExclusion.paths(List(N,N,N),5).toList)
  println(mutualExclusion.paths(List(N,N,N),5).contains:
    List(List(N, N, N), List(T, N, N), List(T, T, N), List(C, T, N), List(N, T, N)))
