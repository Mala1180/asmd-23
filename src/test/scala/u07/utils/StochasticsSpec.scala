package u07.utils

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper

class StochasticsSpec extends AnyFunSuite:
  import Stochastics.given

  val choices = Set( 1.0->"a", 2.0->"b", 3.0->"c")

  test("Choices should correctly give cumulative list"):
      Stochastics.cumulative(choices.toList) shouldBe
        List((1.0,"a"), (3.0,"b"), (6.0,"c"))

  test("Choices should correctly draw"):
    val map = Stochastics.statistics(choices, 10000)

    map("a") shouldBe 1666 +- 500
    map("b") shouldBe 3333 +- 500
    map("c") shouldBe 5000 +- 500
