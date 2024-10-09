package u08.examples

import u08.modelling.{CTMC, CTMCExperiment}
import u08.modelling.CTMC.*
import scala.math.BigDecimal.double2bigDecimal
import java.util.Random
import scala.u08.utils.Time

object SimpleExponentialExperiment extends App with de.sciss.chart.module.Charting:

  enum State:
    case IDLE, DONE

  export State.*
  export u08.modelling.CTMCExperiment.*
  export u08.modelling.CTMCSimulation.*

  def simpleAutomaton: CTMC[State] = CTMC.ofTransitions(
    Transition(IDLE, 1.0 --> DONE),
    Transition(DONE, 1.0 --> DONE)
  )

  val data =
    for
      t <- 0.1 to 10.0 by 0.1
      p = simpleAutomaton.experiment(
        runs = 19000,
        prop = simpleAutomaton.eventually(_ == DONE),
        s0 = IDLE,
        timeBound = t.toDouble)
    yield (t, p)

  Time.timed:
    println:
      data.mkString("\n")

  given ChartTheme = ChartTheme.Default
  val chart = de.sciss.chart.api.XYLineChart(data)
  chart.show("Probability")