package pc.examples

export pc.modelling.PetriNet
import pc.utils.MSet

object PNMutualExclusion:

  enum Place:
    case N, T, C
    
  export Place.*
  export pc.modelling.PetriNet.*
  export pc.modelling.SystemAnalysis.*
  export pc.utils.MSet

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
