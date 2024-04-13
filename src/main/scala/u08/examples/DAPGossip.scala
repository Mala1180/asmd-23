package u08.examples

import java.util.Random

import u08.modelling.{CTMCSimulation, DAP, DAPGrid}
import u08.modelling.CTMCSimulation.*
import scala.u08.utils.{Grids, MSet}

object DAPGossip:
  enum Place:
    case A,B,C
  type ID = (Int, Int)
  export Place.*
  export u08.modelling.DAP.*
  export u08.modelling.DAPGrid.*
  export u08.modelling.CTMCSimulation.*

  val gossipRules = DAP[Place](
    Rule(MSet(A,A), m=>1000,  MSet(A),  MSet()),   // a|a --1000--> a
    Rule(MSet(A),   m=>1,     MSet(A),  MSet(A)),       // a --1--> a|^a
  )
  val gossipCTMC = DAP.toCTMC[ID, Place](gossipRules)
  val net = Grids.createRectangularGrid(5, 5)
  // an `a` initial on top left
  val state = State[ID,Place](MSet(Token((0, 0), A)), MSet(), net)

@main def mainDAPGossip =
  import DAPGossip.*
  gossipCTMC.newSimulationTrace(state,new Random).take(50).toList.foreach: step =>
    println(step._1) // print time
    println(DAPGrid.simpleGridStateToString[Place](step._2, A)) // print state, i.e., A's