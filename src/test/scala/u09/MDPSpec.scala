package u09

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import u09.examples.TwoWaysMDP
import u09.examples.TwoWaysMDP.*
import u09.examples.TwoWaysMDP.Pos
import u09.examples.TwoWaysMDP.Move.*

class QFunctionSpec extends AnyFunSuite:

  test("QF should properly get"):
    TwoWaysMDP.rl.qfTW()(Pos(0),LEFT) shouldBe 1.0
    TwoWaysMDP.rl.qfTW()(Pos(20),LEFT) shouldBe 0.0

  test("QF should properly update"):
    TwoWaysMDP.rl.qfTW().update(Pos(0),LEFT,100)(Pos(0),LEFT) shouldBe 100
    TwoWaysMDP.rl.qfTW().update(Pos(10),LEFT,100)(Pos(10),LEFT) shouldBe 0.0

class MDPSpec extends AnyFunSuite:

  test("MDP should properly take"):
    TwoWaysMDP.rl.mdpTW()(Pos(4),RIGHT) shouldBe (-0.2,Pos(5))
    TwoWaysMDP.rl.mdpTW()(Pos(-4),LEFT) shouldBe (10.0,Pos(-5))

class RLSpec extends AnyFunSuite:

  val rl = TwoWaysMDP.rl.rlTW()

  test("QRL should properly updated QF"):
    val qf = rl.updateQ(rl.system.initial, TwoWaysMDP.rl.qfTW())._2
    qf(Pos(0),LEFT) shouldBe 0.85

  test("QRL should properly run an episode"):
    val qf = rl.learn(100,100,TwoWaysMDP.rl.qfTW())
    qf(Pos(10),LEFT) shouldBe 0.0
    qf(Pos(-4),LEFT) shouldBe 10.0
    (-5 to 10).forall(i => qf.bestPolicy(Pos(i))==LEFT) shouldBe true

  test("QRL should properly execute A run after learning"):
    val qf = rl.learn(100,100,TwoWaysMDP.rl.qfTW())
    rl.system.run(qf.bestPolicy).toList shouldBe List(
      (LEFT,Pos(-1)),
      (LEFT,Pos(-2)),
      (LEFT,Pos(-3)),
      (LEFT,Pos(-4)),
      (LEFT,Pos(-5)))
