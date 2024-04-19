package pc.utils

import org.scalatest.FlatSpec

class StochasticsSpec extends FlatSpec{

  val choices = Set( 1.0->"a", 2.0->"b", 3.0->"C")

  "Choices" should "correctly give cumulative list" in {
    assertResult(
      List((1.0,"a"), (3.0,"b"), (6.0,"C"))
    )(
      Stochastics.cumulative(choices.toList)
    )
  }

  "Choices" should "correctly draw" in {
    val map = Stochastics.statistics(choices, 10000)

    assert(map("a")>1600)
    assert(map("a")<1720)
    assert(map("b")>3000)
    assert(map("b")<3500)
    assert(map("C")>4800)
    assert(map("C")<5200)
  }
}
