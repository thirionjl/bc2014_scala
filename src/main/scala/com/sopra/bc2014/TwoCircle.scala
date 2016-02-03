package com.sopra.bc2014

object TwoCircle {

  object Intersection extends Enumeration {
    type Intersection = Value
    val ZERO, ONE, TWO, INFINITY = Value
  }

  case class Point(x: BigDecimal, y: BigDecimal) {
    def this(x: Int, y: Int) {
      this(BigDecimal(x), BigDecimal(y))
    }
    def squareDistanceTo(p: Point) = (x - p.x).pow(2) + (y - p.y).pow(2)
  }


  case class Circle(center: Point, radius: BigDecimal) {

    import Intersection._

    def this(center: Point, radius: Int) {
      this(center, BigDecimal(radius))
    }

    def getIntersections(c: Circle): Intersection = {
      val squaredDistance = center.squareDistanceTo(c.center)
      val squaredRadiusDelta = (radius - c.radius).pow(2)
      val squaredRadiusSum = (radius + c.radius).pow(2)


      if (squaredRadiusDelta > squaredDistance || squaredDistance > squaredRadiusSum) ZERO
      else if (squaredRadiusDelta > squaredDistance || squaredDistance == squaredRadiusSum) {
        if (center == c.center) INFINITY else ONE
      }
      else TWO
    }
  }

  def main(args: Array[String]) {
    val c1 = new Circle(new Point(3, 3), 2)
    val c2 = new Circle(new Point(3, 3), 2)
    println(s"${c1.getIntersections(c2)} intersection(s)")
  }


}
