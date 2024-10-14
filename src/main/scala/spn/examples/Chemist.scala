package spn.examples

import de.sciss.chart.api.{XYSeries, XYSeriesCollection}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartPanel}
import spn.SPN
import u07.modelling.CTMC
import utils.MSet

import java.util.Random
import javax.swing.JFrame

object Chemist extends App:

  private enum Element:
    case A, B, D, E, X, Y

  import Element.*

  private val numberOfA: Int = 1000
  private val numberOfB: Int = 3000
  private val numberOfX: Int = 1000
  private val numberOfY: Int = 1000

  private val k1: Double = 1.0 // 1.0
  private val k2: Double = 1.0 // 0.002
  private val k3: Double = 1.0 // 0.01
  private val k4: Double = 1.0 // 0.1

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
//  private val brussellatorSPN: SPN[Element] =
//    (from(A) to X withRate rateOfX) ++
//      (from(X, X, Y) to (X, X, X) withRate rateOfAutocatalyticReaction) ++
//      (from(B, X) to (Y, D) withRate rateOfConversionYD) ++
//      (from(X) to E withRate rateOfDegradation)

  private val brussellatorSPN: SPN[Element] =
    (from(A) to X withRate 1) ++
      (from(X, X, Y) to (X, X, X) withRate 1) ++
      (from(B, X) to (Y, D) withRate 1) ++
      (from(X) to E withRate 1)

  private val simulation = toCTMC(brussellatorSPN)
    .newSimulationTrace(
      MSet.ofList(
        List.fill(numberOfA)(A)
          concat List.fill(numberOfB)(B)
          concat List.fill(numberOfX)(X)
          concat List.fill(numberOfY)(Y)
      ),
      new Random
    )
    .take(300)
    .toList

  private val dataX = new XYSeries("X")
  for event <- simulation
  yield dataX.add(event.time, event.state.asList.count(_ == X))

  private val dataY = new XYSeries("Y")
  for event <- simulation
  yield dataY.add(event.time, event.state.asList.count(_ == Y))

  private val dataset = new XYSeriesCollection()
  dataset.addSeries(dataX) // Add the first line
  dataset.addSeries(dataY) // Add the second line

  private val chart = ChartFactory.createXYLineChart(
    "Brussellator",
    "Time",
    "Molecules",
    dataset,
    PlotOrientation.VERTICAL,
    true, // Include legend
    true, // Tooltips
    false // URLs
  )
  chart.getXYPlot.getRangeAxis.setRange(950, 1050)

  private val chartPanel = new ChartPanel(chart)

  private val frame = new JFrame("Scala-Chart Multi-Line Example")
  frame.setContentPane(chartPanel)
  frame.pack()
  frame.setVisible(true)
