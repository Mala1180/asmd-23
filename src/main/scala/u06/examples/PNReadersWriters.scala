package scala.u06.examples

export u06.modelling.PetriNet
import scala.u06.modelling.*
import u06.utils.MSet

object PNReadersWriters:

  enum Place:
    case START, CHOICE, WAIT_READ, WAIT_WRITE, ME, READING, WRITING

  export Place.*
  export u06.modelling.PetriNet.*
  export u06.modelling.SystemAnalysis.*
  export u06.utils.MSet

  // DSL-like specification of a Petri Net
  def pnRW: System[Marking[Place]] = PetriNet[Place](
    MSet(START) ~~> MSet(CHOICE),
    MSet(CHOICE) ~~> MSet(WAIT_READ),
    MSet(CHOICE) ~~> MSet(WAIT_WRITE),
    MSet(WAIT_READ, ME) ~~> MSet(ME, READING),
    MSet(WAIT_WRITE, ME) ~~> MSet(WRITING) ^^^ MSet(READING),
    MSet(READING) ~~> MSet(START),
    MSet(WRITING) ~~> MSet(START, ME)
  ).toSystem

@main def mainPNReadersWriters(): Unit =
  import PNReadersWriters.*
  // example usage
  println(pnRW.paths(MSet(START, START, ME), 7).toList.mkString("\n"))
