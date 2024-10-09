package u08.modelling

import u08.modelling.CTMC.*
import scala.u08.utils.MSet

object SPN:

  // pre-conditions, rate, effects, inhibition
  case class Trn[P](
    cond: MSet[P],
    rate: MSet[P]=>Double,
    eff: MSet[P],
    inh: MSet[P])

  type SPN[P] = Set[Trn[P]]

  def toCTMC[P](spn: SPN[P]): CTMC[MSet[P]] =
    m =>
      for
        Trn(cond, rate, eff, inh) <- spn
        if m disjoined inh
        r = rate(m)
        out <- m extract cond
      yield Action(r, out union eff)

  def apply[P](transitions: Trn[P]*): SPN[P] = transitions.toSet