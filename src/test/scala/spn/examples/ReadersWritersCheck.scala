package spn.examples

import org.scalacheck.*
import org.scalacheck.Prop.forAll
import spn.SPN.toCTMC
import spn.examples.ReadersWriters.Place.*
import spn.examples.ReadersWriters.{Place, createRWSPN}
import u07.modelling.CTMCSimulation.{Event, newSimulationTrace}
import utils.MSet

/** Verifies that the Stochastic Petri Nets generated by the LLMs have correct properties. */
object ReadersWritersCheck extends Properties("Stochastic Readers and Writers"):

  val randomRates: Gen[List[Double]] = Gen.listOfN(4, Gen.choose(0.0, 100.0))
  val randomTokens: Gen[Int] = Gen.choose(1, 10)

  val simulations: Gen[List[Event[MSet[Place]]]] =
    for
      rates <- randomRates
      initialTokens <- randomTokens
    yield toCTMC(createRWSPN(rates(0), rates(1), rates(2), rates(3)))
      .newSimulationTrace(MSet.ofList(List.fill(initialTokens)(START) concat List(ME)), java.util.Random())
      .take(100)
      .toList

  property("There can be only one writer at a time") = {
    forAll(simulations) { simulation =>
      simulation forall { _.state.filter(_ == WRITING).size <= 1 }
    }
  }

  property("While a writer is writing, no reader can read") = {
    forAll(simulations) { simulation =>
      simulation forall { event =>
        event.state.filter(_ == WRITING).size == 0 || event.state.filter(_ == READING).size == 0
      }
    }
  }
