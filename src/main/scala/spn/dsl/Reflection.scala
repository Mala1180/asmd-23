package spn.dsl

import spn.SPN.{SPN, Trn}

object Reflection:

  /** Reflects the input to the REPL returning a SPN.
    * If the REPL is not started yet, it waits until it is started.
    * @param input the input to evaluate
    * @return the [[SPN]]
    * @throws IllegalArgumentException if the input is malformed
    */
  def reflect(input: String): SPN[Any] =
    if input.isBlank || input.isEmpty then throw new IllegalArgumentException("Empty input")
    else
      val imports =
        """import spn.SPN.*
          |import spn.dsl.DSL.{*, given}
          |""".stripMargin
      try
        val res = dotty.tools.repl.ScriptEngine().eval(imports + input)
        res match
          case spn: SPN[_] => spn.asInstanceOf[SPN[Any]]
          case trn: Trn[_] => Set(trn.asInstanceOf[Trn[Any]])
          case _ => throw new IllegalArgumentException("Invalid SPN")
      catch case e: Exception => throw new IllegalArgumentException(e.getMessage)
