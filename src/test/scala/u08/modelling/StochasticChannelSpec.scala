package u08.modelling;

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class StochasticChannelSpec extends AnyFunSuite:

  import u08.examples.StochasticChannel.*

  test("Stochastic channel should correctly draw transitions") {
    stocChannel.transitions(IDLE) shouldBe Set(Action(1.0, SEND))
    stocChannel.transitions(SEND) shouldBe Set(Action(100_000, SEND), Action(200_000, DONE), Action(100_000, FAIL))
    stocChannel.transitions(FAIL) shouldBe Set(Action(100_000, IDLE))
    stocChannel.transitions(DONE) shouldBe Set(Action(1, DONE))
  }
