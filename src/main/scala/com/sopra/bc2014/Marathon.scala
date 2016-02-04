package com.sopra.bc2014

import com.sopra.bc2014.util.ArithmeticUtils.lcm

object Marathon {


  def pairProds(speeds: List[Int]): List[Int] = {
    val x: List[Int] = speeds.combinations(2).map(_.product).toList
    x
  }

  def main(args: Array[String]) {
    val speedsInKmh : List[Int] = List(12, 14, 5)
    val pairProducts: Array[Int] = pairProds(speedsInKmh).toArray
    val durationInMinutes = 60.0 * lcm(pairProducts) / speedsInKmh.product


    println(f"Runners will all cross the line at same time after $durationInMinutes%.1f minutes")
  }

}
