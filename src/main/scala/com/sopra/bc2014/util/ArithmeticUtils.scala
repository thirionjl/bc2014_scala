package com.sopra.bc2014.util

import com.google.common.math.IntMath

import scala.annotation.tailrec

object ArithmeticUtils {

  def lcm(ints: Array[Int]): Int = {
    @tailrec
    def loop(acc: Int, pos: Int, ints: Array[Int]): Int = {
      if (pos >= ints.length - 1) acc
      else {
        val newAcc = acc * ints(pos) / gcd(acc, ints(pos))
        loop(newAcc, pos + 1, ints)
      }
    }
    loop(1, 0, ints)
  }

  def gcd(a: Int, b: Int): Int = IntMath.gcd(a, b);

}
