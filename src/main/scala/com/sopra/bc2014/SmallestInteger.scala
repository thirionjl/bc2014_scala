package com.sopra.bc2014

object SmallestInteger {

  def allDivide(n: Long, m: Long): Boolean = {
    Iterator.range(m.toInt, 2, -1).find(n % _ != 0).isEmpty
  }

  def main(args: Array[String]) {
    val X: Long = 20
    Iterator.from(1).find(allDivide(_, X)) foreach (r => println(f"Solution is $r%,d"))
  }


}
