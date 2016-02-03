package com.sopra.bc2014.dp

import scala.math._

object Skis extends DynamicProgramming {
  val r = scala.util.Random
  val skis = (1 to 20).map(x => r.nextInt(100) + 100)
  val skieurs = (1 to 10).map(x => r.nextInt(100) + 100)

  type State = (Int, Set[Int]) // Skieur i + Set de skis restants
  type Choice = Int // Index du ski choisi

  override val periods: Int = skieurs.size
  override def choices(s: State): collection.Set[Choice] = s._2
  override def transition(s: State, c: Choice): State = (s._1 + 1, s._2 - c)
  override def valuation(previousValuation: Int, previousState: State, c: Choice, newState: State): Int = previousValuation + abs(skis(c) - skieurs(previousState._1))
  override def isBetter(previousValuation: Int, newValuation: Int): Boolean = newValuation < previousValuation
  override def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[State] = {
    if (periods == periodNr) {
      val (bestState, _) = row.minBy { case (_, valuation) => valuation }
      Some(bestState)
    } else {
      None
    }
  }
  override val initialValuation: Int = 0
  override val initialState: State = (0, (0 until skis.size).toSet)

  def main(args: Array[String]) {
    val (topValuation, path) = solve()
    println(s"topValuation = $topValuation")
    println(path.map(si => skis(si)))
  }
}
