package spn.examples

import spn.SPN.{SPN, toCTMC}
import spn.SPNSimulationPlot.plot
import u07.modelling.CTMCSimulation.{Event, newSimulationTrace}
import utils.MSet

object ReadersWriters extends App:

  enum Place:
    case START, CHOICE, WAIT_READ, WAIT_WRITE, READING, WRITING, ME

  import Place.*
  import spn.dsl.DSL.{*, given}
  def createRWSPN(
      readingRate: Double,
      writingRate: Double,
      exitReadingRate: Double,
      exitWritingRate: Double
  ): SPN[Place] =
    (from(START) to CHOICE withRate 1.0) ++
      (from(CHOICE) to WAIT_READ withRate readingRate) ++
      (from(CHOICE) to WAIT_WRITE withRate writingRate) ++
      (from(WAIT_READ, ME) to (ME, READING) withRate readingRate) ++
      (from(WAIT_WRITE, ME) to WRITING withRate writingRate inhibitedBy READING) ++
      (from(READING) to START withRate exitReadingRate) ++
      (from(WRITING) to (START, ME) withRate exitWritingRate)

  private val rwSPN: SPN[Place] = createRWSPN(1, 100, 1, 0.01)
  def simulation: List[Event[MSet[Place]]] = toCTMC(rwSPN)
    .newSimulationTrace(MSet(START, START, START, START, ME), java.util.Random())
    .take(100)
    .toList

  simulation.plot()
  simulation foreach { println(_) }
