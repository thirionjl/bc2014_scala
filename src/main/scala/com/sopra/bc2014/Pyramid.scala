package com.sopra.bc2014

import scala.annotation.tailrec

object Pyramid {

  def pyramidSize(n: Long): Long = {
    val b = Iterator.from(1).find(b => b * (b + 1) > 2 * n).get - 1
    b * (b + 1) / 2
  }

  def arrangements(p: Long, n: Long): Long = {
    @tailrec
    def go(acc: Long, n: Long, l: Long): Long = if (n == l) acc else go(acc * n, n - 1, l)
    if (p <= n) go(1, n, n - p) else sys.error(s"p($p) must be greater than n($n")
  }

  def main(args: Array[String]) {
    val n = 13
    val p = pyramidSize(n)
    println(f"There are ${arrangements(p, n)}%,d possible arrangements")
  }

}
