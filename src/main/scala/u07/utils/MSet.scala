package u07.utils

// A multiset datatype
trait MSet[A] extends (A => Int):
  def union(m: MSet[A]): MSet[A]
  def diff(m: MSet[A]): MSet[A]
  def disjoined(m: MSet[A]): Boolean
  def size: Int
  def matches(m: MSet[A]): Boolean
  def extract(m: MSet[A]): Option[MSet[A]]
  def asList: List[A]
  def asMap: Map[A,Int]
  def iterator: Iterator[A]

// Functional-style helpers/implementation
object MSet:
  // Factories
  def apply[A](l: A*): MSet[A] = new MSetImpl(l.toList)
  def ofList[A](l: List[A]): MSet[A] = new MSetImpl(l)
  def ofMap[A](m: Map[A,Int]): MSet[A] = MSetImpl(m)

  // Hidden reference implementation
  private case class MSetImpl[A](asMap: Map[A,Int]) extends MSet[A]:
    def this(list: List[A]) =
      this(list.groupBy(a=>a).map{case (a,n) => (a, n.size)})
    override val asList =
      asMap.toList.flatMap{case (a,n) => List.fill(n)(a)}

    override def apply(v1: A) = asMap.getOrElse(v1,0)
    override def union(m: MSet[A]) = new MSetImpl[A](asList ++ m.asList)
    override def diff(m: MSet[A]) = new MSetImpl[A](asList diff m.asList)
    override def disjoined(m: MSet[A]) = (asList intersect m.asList).isEmpty
    override def size = asList.size
    override def matches(m: MSet[A]) = extract(m).isDefined
    override def extract(m: MSet[A]) =
      Some(this diff m) filter (_.size == size - m.size)
    override def iterator = asMap.keysIterator
    override def toString = s"{${asList.mkString("|")}}"
