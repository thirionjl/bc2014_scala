package com.sopra.bc2014

import scalaxy.streams.optimize
import scalaxy.streams.strategy.aggressive

object Pythagore {

  def solve(x: Int): Seq[String] = {

    optimize {
      val max: Int = x / 2 + 1
      for {
        a <- 0 until max
        b <- 0 until max
        c <- 0 until max if (a + b + c == x && a * a + b * b == c * c)
      } yield s"{a=$a b=$b c=$c}"
    }
  }

  def main(args: Array[String]) {
    val start = System.currentTimeMillis()
    solve(5000) foreach (println(_))
    println(System.currentTimeMillis() - start)
  }

}
