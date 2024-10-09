package u07.examples

import spn.SPN
import u07.modelling.CTMC
import utils.MSet

import java.util.Random

object StochasticMutualExclusion extends App:
  // Specification of my data-type for states
  enum Place:
    case N,T,C

  export Place.*
  export u07.modelling.CTMCSimulation.*
  export SPN.*

  val spn = SPN[Place](
    Trn(MSet(N), m => 1.0,   MSet(T),  MSet()),
    Trn(MSet(T), m => m(T),  MSet(C),  MSet(C)),
    Trn(MSet(C), m => 2.0,   MSet(),   MSet()))

  println:
    toCTMC(spn).newSimulationTrace(MSet(N,N,N,N),new Random)
      .take(20)
      .toList.mkString("\n")