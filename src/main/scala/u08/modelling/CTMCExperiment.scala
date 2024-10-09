package u08.modelling

import java.util.Random

object CTMCExperiment:

  import CTMCSimulation.*

  opaque type Property[A] = Trace[A] => Boolean

  given rnd: Random = new Random

  extension [S](self: CTMC[S])
    // globally is simply achieved by equivalence not G x= F not x
    def eventually[A](filt: A => Boolean): Property[A] =
      trace => trace exists (e => filt(e.state))

    // takes a property and makes it time bounded by the magics of streams
    def bounded[A](timeBound: Double)(prop: Property[A]): Property[A] =
      trace => prop(trace takeWhile (_.time <= timeBound))

    // a PRISM-like experiment, giving a statistical result (in [0,1])
    def experiment(runs: Int, prop: Property[S], s0: S, timeBound: Double): Double =
      (0 until runs).count: _ =>
        bounded(timeBound)(prop)(self.newSimulationTrace(s0 ,rnd))
      .toDouble/runs