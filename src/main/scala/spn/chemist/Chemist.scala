package spn.chemist

import de.sciss.chart.api.{ChartTheme, XYLineChart}
import spn.SPN
import u07.modelling.CTMC
import utils.MSet

import java.util.Random

object Chemist extends App:

  enum Element:
    case A, B, D, E, X, Y

  import Element.*

  private val numberOfA: Int = 20
  private val numberOfB: Int = 40

  private val k1: Double = 1.0
  private val k2: Double = 0.002
  private val k3: Double = 0.01
  private val k4: Double = 0.1

  private val rateOfX: MSet[Element] => Double =
    mset =>
      val nA = mset.asList.count(_ == A)
      k1 * nA

  private val rateOfAutocatalyticReaction: MSet[Element] => Double =
    mset =>
      val nX = mset.asList.count(_ == X)
      val nY = mset.asList.count(_ == Y)
      k2 * nX * (nX - 1) * nY

  private val rateOfConversionYD: MSet[Element] => Double =
    mset =>
      val nX = mset.asList.count(_ == X)
      val nB = mset.asList.count(_ == B)
      k3 * nX * nB

  private val rateOfDegradation: MSet[Element] => Double =
    mset =>
      val nX = mset.asList.count(_ == X)
      k4 * nX

  export u07.modelling.CTMCSimulation.*
  export SPN.*

  import spn.dsl.DSL.{*, given}
  private val brussellatorSPN: SPN[Element] =
    (from(A) to X withRate rateOfX) ++
      (from(X, X, Y) to (X, X, X) withRate rateOfAutocatalyticReaction) ++
      (from(B, X) to (Y, D) withRate rateOfConversionYD) ++
      (from(X) to E withRate rateOfDegradation)

  private val simulation = toCTMC(brussellatorSPN)
    .newSimulationTrace(MSet.ofList(List.fill(numberOfA)(A) concat List.fill(numberOfB)(B)), new Random)
    .take(200)
    .toList

  private val dataX: Seq[(Double, Double)] =
    for event <- simulation
    yield (event.time, event.state.asList.count(_ == X))
  private val dataY: Seq[(Double, Double)] =
    for event <- simulation
    yield (event.time, event.state.asList.count(_ == Y) * 1.0)

  given ChartTheme = ChartTheme.Default
  private val chartX = XYLineChart(dataX)
  private val chartY = XYLineChart(dataY)
  chartX.show("X")
  chartY.show("Y")
