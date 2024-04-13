package scala.u08.utils

object Grids:
  
  type Cell = (Int, Int)
  // creates the useful grid-like neighboring relation
  def createRectangularGrid(n:Int, m:Int): Map[Cell, Set[Cell]] =
    (for
      i <- (0 to n-1).toSet
      j <- 0 to m-1
      (k,l) <- Set( (i-1,j), (i+1,j), (i,j-1), (i,j+1))
      if k>=0 && k<n && l>=0 && l<m
    yield ( (i,j), (k,l) )).groupBy(_._1).view.mapValues(_ map (_._2)).toMap

  // pretty printing a grid
  def gridLikeToString[S](rows: Int, cols: Int, obs: Cell => String): String =
    (0 to rows) map (j => (0 to cols) map (i => obs(i,j)) mkString("\t")) mkString("\n")