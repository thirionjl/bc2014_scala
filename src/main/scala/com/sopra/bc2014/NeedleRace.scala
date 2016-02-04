package com.sopra.bc2014

import java.time.LocalTime

object NeedleRace {

  def areNeedleSuperimposed(time: LocalTime): Boolean = time.getMinute == time.getHour % 12 * 5

  def main(args: Array[String]) {
    val start = LocalTime.of(0, 1)
    val stop = LocalTime.of(22, 24)

    val timeline = Iterator.iterate(start)(_.plusMinutes(1)).takeWhile(time => !time.isAfter(stop))
    val crossings = timeline.count(areNeedleSuperimposed)

    println(f"There are $crossings%d between $start%s and $stop%s")
  }

}
