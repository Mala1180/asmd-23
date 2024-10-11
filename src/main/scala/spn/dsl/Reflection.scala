package spn.dsl

import spn.SPN.{SPN, Trn}

object Reflection:

  /** Reflects the input to the REPL returning a SPN.
    * If the REPL is not started yet, it waits until it is started.
    * @param input the input to evaluate
    * @return the [[SPN]]
    * @throws IllegalArgumentException if the input is malformed
    */
  def reflect(input: String): Option[SPN[Any]] =
    if input.isBlank || input.isEmpty then throw new IllegalArgumentException("Empty input")
    else
      val imports =
        """import spn.SPN.*
          |import spn.dsl.DSL.{*, given}
          |""".stripMargin
      try
        val res = dotty.tools.repl.ScriptEngine().eval(imports + input)
        res match
          case spn: SPN[_] => Some(spn.asInstanceOf[SPN[Any]])
          case trn: Trn[_] => Some(Set(trn.asInstanceOf[Trn[Any]]))
          case _ => None
      catch case e: Exception => None
