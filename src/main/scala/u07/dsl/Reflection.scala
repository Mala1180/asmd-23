package u07.dsl

import u07.modelling.SPN.{SPN, Trn}

object Reflection:

  /** Reflects the input to the REPL returning a SPN.
    * If the REPL is not started yet, it waits until it is started.
    * @param input the input to evaluate
    * @return the [[SPN]]
    * @throws IllegalArgumentException if the input is malformed
    */
  def reflect(input: String): SPN[String] =
    if input.isBlank || input.isEmpty then throw new IllegalArgumentException("Empty input")
    else
      val imports =
        """import u07.modelling.SPN.SPN
          |import u07.dsl.DSL.{*, given}
          |""".stripMargin
      try
        val res = dotty.tools.repl.ScriptEngine().eval(imports + input)
        res match
          case spn: SPN[String] => spn
          case trn: Trn[String] => Set(trn)
      catch case e: Exception => throw new IllegalArgumentException(e.getMessage)
