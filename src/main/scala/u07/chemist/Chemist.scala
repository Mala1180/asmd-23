package scala.u07.chemist

import de.sciss.chart.api.*
import u07.modelling.{CTMC, SPN}
import u07.utils.MSet

import java.util.Random

object Chemist extends App:

  enum Element:
    case A, B, D, E, X, Y

  export Element.*

  export u07.modelling.CTMCSimulation.*
  export u07.modelling.SPN.*

  private val brussellatorPetriNet: SPN[Element] = SPN[Element](
    Trn(MSet(A), m => 50.0, MSet(X), MSet()),
    Trn(MSet(X, X, Y), m => 34.8, MSet(X, X, X), MSet()),
    Trn(MSet(B, X), m => 30.0, MSet(Y, D), MSet()),
    Trn(MSet(X), m => 3.0, MSet(E), MSet())
  )

  private val simulation = toCTMC(brussellatorPetriNet)
    .newSimulationTrace(MSet.ofList(List.fill(20)(A) concat List.fill(40)(B)), new Random)
    .take(100)
    .toList

  simulation.foreach(event => event.state.asList.groupBy(identity).view.mapValues(_.size).toMap)

  val dataX: Seq[(Double, Double)] =
    for event <- simulation
    yield (event.time, event.state.asList.count(_ == X) * 1.0)
  val dataY: Seq[(Double, Double)] =
    for event <- simulation
    yield (event.time, event.state.asList.count(_ == Y) * 1.0)
  given ChartTheme = ChartTheme.Default
  val chartX = XYLineChart(dataX)
  val chartY = XYLineChart(dataY)
  chartX.show("Brussellator X")
  chartY.show("Brussellator Y")
