package u07.features

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.matchers.should.Matchers.*
import spn.dsl.Reflection.reflect
import spn.SPN.SPN
import utils.MSet

import scala.compiletime.uninitialized

object SPNDSLSteps extends ScalaDsl with EN:

  var input: String = uninitialized
  var spn: Option[SPN[String]] = None
  var error: Exception = uninitialized

  Given("the input {string}")((strSpn: String) => input = strSpn)

  When("I create the SPN") {
    try spn = Some(reflect(input))
    catch case e: IllegalArgumentException => error = IllegalArgumentException(e.getMessage)
  }

  Then("I should obtain a SPN with {int} transitions") { (size: Int) =>
    spn.get.size shouldBe size
  }

  And("the transition is {string} to {string} with markovian rate {double}") {
    (from: String, to: String, rate: Double) =>
      val trn = spn.get
      trn.head.cond.asList shouldBe MSet(from).asList
      trn.head.rate(trn.head.cond) shouldBe rate
      trn.head.eff shouldBe MSet(to)
  }

  And("inhibited by {string}") { (inhibited: String) =>
    val trn = spn.get
    trn.head.inh.asList shouldBe MSet(inhibited).asList
  }

  And("the transition consumes {int} tokens from {string}") { (tokens: Int, place: String) =>
    val trn = spn.get
    trn.head.cond.asList.count(_ == place) shouldBe tokens
  }

  And("the transition produces {int} tokens in {string}") { (tokens: Int, place: String) =>
    val trn = spn.get
    trn.head.eff.asList.count(_ == place) shouldBe tokens
  }

  Then("I should obtain an IllegalArgumentException") {
    println("TEST" + spn)
    error shouldBe a[IllegalArgumentException]
  }
