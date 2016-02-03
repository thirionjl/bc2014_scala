package com.sopra.bc2014.util

object TraversableOnceOps {

  implicit class TraversableIntOps(val value: scala.collection.TraversableOnce[Int]) {

    def average(): Double = {
      val (sum, cnt) = value.foldLeft((0, 0)) {
        case ((s, c), a) => (s + a, c + 1)
      }
      if (cnt == 0) 0 else sum.toDouble / cnt
    }
  }

}
