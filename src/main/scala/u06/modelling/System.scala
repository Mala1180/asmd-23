package scala.u06.modelling

// The definition of a Rewrite System, as a function: S => Set[S]
trait System[S]:
  def next(a: S): Set[S]

// Our factory of Systems
object System:

  // The most general case, an intensional one
  def ofPartialFunction[S](f: PartialFunction[S, Set[S]]): System[S] = s =>
    f.applyOrElse(s, _ => Set[S]())

  // Extensional specification
  def ofRelation[S](rel: Set[(S, S)]): System[S] = ofPartialFunction: s =>
    rel collect:
      case (`s`, s2) => s2

  // Extensional with varargs
  def ofTransitions[S](rel: (S, S)*): System[S] =
    ofRelation(rel.toSet)