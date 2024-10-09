package spn.dsl

import spn.SPN.{SPN, Trn}

object Conversions:

  given transitionToSPN[P]: Conversion[Trn[P], SPN[P]] with
    def apply(trn: Trn[P]): SPN[P] = Set(trn)
