package scala.u08.utils

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.scalactic.Tolerance.convertNumericToPlusOrMinusWrapper

import scala.u08.utils.Stochastics.{*, given}

class StochasticsSpec extends AnyFunSuite:

  val choices = Set( 1.0->"a", 2.0->"b", 3.0->"C")

  test("Choices should correctly give cumulative list") {
      Stochastics.cumulative(choices.toList) shouldBe
        List((1.0,"a"), (3.0,"b"), (6.0,"C"))
  }

  test("Choices should correctly draw") {
    val map = Stochastics.statistics(choices, 10000)

    map("a") shouldBe 1666 +- 500
    map("b") shouldBe 3333 +- 500
    map("C") shouldBe 5000 +- 500
  }
