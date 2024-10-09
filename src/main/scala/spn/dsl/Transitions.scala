package spn.dsl

import spn.SPN.{SPN, Trn}
import utils.MSet

import scala.annotation.targetName

object Transitions extends App:

  infix def from[P](initialPlaces: P*): Trn[P] =
    Trn(
      MSet.ofList(initialPlaces.toList),
      * => 1,
      MSet(),
      MSet()
    )

  extension [P](trn: Trn[P])
    infix def to(finalPlaces: P*): Trn[P] = trn.copy(eff = MSet.ofList(finalPlaces.toList))
    infix def withRate(rate: Double): Trn[P] = trn.copy(rate = _ => rate)
    infix def withRate(rate: MSet[P] => Double): Trn[P] = trn.copy(rate = rate)
    infix def inhibitedBy(inhibitors: P*): Trn[P] = trn.copy(inh = MSet.ofList(inhibitors.toList))

  extension [P](spn: SPN[P])
    @targetName("union")
    infix def ++(trn: Trn[P]): SPN[P] = spn + trn
    infix def and(trn: Trn[P]): SPN[P] = ++(trn)
