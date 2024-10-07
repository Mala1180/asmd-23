package u07.dsl

import u07.modelling.SPN.{SPN, Trn}

object Conversions:

  given transitionToSPN[P]: Conversion[Trn[P], SPN[P]] with
    def apply(trn: Trn[P]): SPN[P] = Set(trn)
