package scala.u06.examples

import scala.u06.modelling.*
import u06.utils.MSet

object PNMutualExclusion:

  enum Place:
    case N, T, C
    
  export Place.*
  export u06.modelling.PetriNet.*
  export u06.modelling.SystemAnalysis.*
  export u06.utils.MSet

  // DSL-like specification of a Petri Net
  def pnME: System[Marking[Place]] = PetriNet[Place](
    MSet(N) ~~> MSet(T),
    MSet(T) ~~> MSet(C) ^^^ MSet(C),
    MSet(C) ~~> MSet()
  ).toSystem

@main def mainPNMutualExclusion(): Unit =
  import PNMutualExclusion.*
  // example usage
  println(pnME.paths(MSet(N,N),7).toList.mkString("\n"))
