package u06.modelling

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class SystemChannelSpec extends AnyFunSuite:
  import scala.u06.examples.SystemChannel.*

  test("System Channel should properly identify normal forms"):
    channel.normalForm(IDLE) shouldBe false
    channel.normalForm(DONE) shouldBe true

  test("System Channel should properly draw next states"):
    channel.next(IDLE) shouldBe Set(SEND)
    channel.next(SEND) shouldBe Set(SEND, DONE, FAIL)

  test("System Channel should properly generate paths"):
    channel.paths(IDLE, 3) should contain:
      List(IDLE, SEND, SEND)
    channel.completePathsUpToDepth(IDLE, 4) should contain theSameElementsAs:
      List(List(IDLE, SEND, DONE), List(IDLE, SEND, SEND, DONE))