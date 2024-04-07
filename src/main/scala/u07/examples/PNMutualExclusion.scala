package u07.examples

export u07.modelling.PetriNet
import u07.utils.MSet

object PNMutualExclusion:

  enum Place:
    case N,T,C
    
  export Place.*
  export u07.modelling.PetriNet.*
  export u07.modelling.SystemAnalysis.*
  export u07.utils.MSet

  // DSL-like specification of a Petri Net
  def pnME = PetriNet[Place](
    MSet(N) ~~> MSet(T),
    MSet(T) ~~> MSet(C) ^^^ MSet(C),
    MSet(C) ~~> MSet()
  ).toSystem

@main def mainPNMutualExclusion =
  import PNMutualExclusion.*
  // example usage
  println(pnME.paths(MSet(N,N),7).toList.mkString("\n"))
