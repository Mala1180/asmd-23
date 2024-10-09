package scala.u08.utils

object Time:

  // facility to track time, just embed the computation in the input
  def timed[A](v: => A): A =
    val t0 = java.lang.System.nanoTime
    try v finally println("Timed op (msec): "+ (java.lang.System.nanoTime - t0) / 1_000_000)

