package com.sopra.bc2014

// To know if a point (a,b) is inside a polygon, draw the horizontal line passing through that point (of equation: y = b)
// and count how often it intersects the polygon sides but count only intersections that happen on the left (or right) side of the point (Area where x < a)
// If this intersection count is odd (1,3,5...) the point is inside the polygon, else the point is outside
object InOrOut {

  type Point = (Double, Double)

  class Segment(p1: Point, p2: Point) {
    val bottom = if (p1._2 < p2._2) p1 else p2
    val up = if (p1._2 < p2._2) p2 else p1

    // Given a point p draw the horizontal line D passing by p
    // This function tells if there is an intersection between D and current
    // segment and if this intersection happens on the left side of p
    def intersectsHorizontalLineOnTheLeft(p: Point): Boolean = isPassingAtHeight(p._2) && getHorizontalPositionWhenPassingAtHeight(p._2) < p._1

    // Does this segment pass at height "height"
    // Notice that horizontal segments never intersect
    def isPassingAtHeight(height: Double): Boolean = bottom._2 < height && height <= up._2

    // Tells where on he X axis this segment has height "height"
    def getHorizontalPositionWhenPassingAtHeight(height: Double): Double = {
      // Compute line equation, with origin point placed at O(p1.x,p1.y),
      // so that eq. has form: y = a.x
      // Notice that vertical segments have a=Infinite
      val a = (up._2 - bottom._2) / (up._1 - bottom._1)
      // translate "height" relative to the new origin O(p1.x,p1.y)
      val y = height - bottom._2
      // Inject y into line equation to compute horizontal position when
      // height is y
      val x = y / a
      // translate back to origin (0,0)
      bottom._1 + x
    }
    override def toString = "Segment ]" + bottom + ", " + up + "]";
  }

  class Polygon(val points: Point*) {

    def getSides(): Seq[Segment] = {
      val nbVertex = points.size
      for {
        i <- 0 until nbVertex
        j = Integer.remainderUnsigned(i + 1, nbVertex)
      } yield new Segment(points(i), points(j))
    }

    def contains(p: Point): Boolean = {
      val intersections = getSides().view
        .filter(_.intersectsHorizontalLineOnTheLeft(p))
        .map(x => {
          println(s"Intersection on the left with $x");
          x
        }) // debug
        .count(x => true)
      intersections % 2 != 0
    }
  }

  object Polygon {
    def apply(points: Point*): Polygon = new Polygon(points: _*)
  }

  def main(args: Array[String]) {
    val p: Polygon = Polygon(
      (0, 0),
      (5, 1),
      (6, 0),
      (3, 2),
      (3, 5),
      (2, 5),
      (1, 4),
      (0, 3))

    val x: Point = (4, 2)
    val position = if (p.contains(x)) "INSIDE" else "OUTSIDE"
    println(s"Point is $position polygon")
  }


}
