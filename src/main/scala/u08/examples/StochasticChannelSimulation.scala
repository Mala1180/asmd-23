package u08.examples

import java.util.Random
import u08.examples.StochasticChannel.*
import scala.u08.utils.Time

@main def mainStochasticChannelSimulation =
  Time.timed:
    println:
      stocChannel
        .newSimulationTrace(IDLE, new Random)
        .take(10)
        .toList
        .mkString("\n")