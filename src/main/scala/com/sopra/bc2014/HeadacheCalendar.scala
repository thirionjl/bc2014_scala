package com.sopra.bc2014

import java.time.{Month, Year}

import scala.math.Ordered.orderingToOrdered
import scala.math._

object HeadacheCalendar {

  case class Calendar(year: Int, month: Int, day: Int) extends Ordered[Calendar] {

    def compare(that: Calendar): Int = (this.year, this.month, this.day) compare(that.year, that.month, that.day)

    def daysStrictlyBetween(other: Calendar): Int = {
      if (this > other) {
        sys.error("Argument must be in future")
      }
      val daysBetween = Iterator.iterate(next())(_.next()).takeWhile(_ < other).count(x => true)
      max(daysBetween, 0)
    }

    def next(): Calendar = {
      var nextDay = day
      var nextMonth = month
      var nextYear = year

      if (day < daysInMonth()) {
        nextDay += 1
      } else {
        nextDay = 1
        if (month < 12) {
          nextMonth += 1
        } else {
          nextMonth = 1
          nextYear += 1
        }
      }
      Calendar(nextYear, nextMonth, nextDay)
    }

    def daysInMonth(): Int = {
      val monthDayCount = Month.of(month).length(Year.of(year).isLeap)
      val oddDaysSuppl = if (monthDayCount % 2 == 0) 0 else 3
      monthDayCount - 2 * month + oddDaysSuppl
    }

  }

  def main(args: Array[String]) {
    val start = Calendar(2010, 1, 1)
    val stop = Calendar(2014, 9, 1)
    val daysBetween = start.daysStrictlyBetween(stop)
    println(s"There are $daysBetween stricly between $start and $stop")
  }

}
