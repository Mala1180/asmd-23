package u09.examples

import u09.model.QMatrix

object TryQMatrix extends App :

  import u09.model.QMatrix.Move.*
  import u09.model.QMatrix.*

  val rl: QMatrix.Facade = Facade(
    width = 5,
    height = 5,
    initial = (0,0),
    terminal = {case _=>false},
    reward = { case ((1,0),DOWN) => 10; case ((3,0),DOWN) => 5; case _ => 0},
    jumps = { case ((1,0),DOWN) => (1,4); case ((3,0),DOWN) => (3,2) },
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction,"%2.2f"))
  val q1 = rl.makeLearningInstance().learn(10000,100,q0)
  println(rl.show(q1.vFunction,"%2.2f"))
  println(rl.show(s => q1.bestPolicy(s).toString,"%7s"))