package u07.utils

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*

class MSetSpec extends AnyFunSuite{

  test("An empty MSet should have size 0"){
    assert( MSet[Int]().size == 0)
  }

  test("A MSet should be equal to another with just different ordering of elements"){
    assert( MSet(10,20,30,30,15,15) == MSet(10,20,30,15,30,15) )
  }

  test("A MSet should not be equal to another when adding s repetition"){
    assert( MSet(10,20,30,30,15,15) != MSet(10,20,30,15,30,15,5,5) )
  }

  test("A MSet should be equally constructed as List or as Map"){
    assert( MSet(10,20,30,30,15,15) == MSet.ofMap(Map(10->1,20->1,30->2,15->2)) )
  }
}
