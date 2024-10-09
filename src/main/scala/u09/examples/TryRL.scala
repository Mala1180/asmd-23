package u09.examples

import TwoWaysMDP.*

object TryRL extends App:
  val rl = TwoWaysMDP.rl.rlTW()
  var qf = rl.learn(20, 10, TwoWaysMDP.rl.qfTW())

  for
    i <- -5 to 10
  do
    println:
      (i, qf.actions.maxBy(qf(Pos(i), _)), qf.actions.map(qf(Pos(i), _)).max)
