package com.sopra.bc2014.dp

import scala.collection.Iterator

object Knipsack extends DynamicProgramming {
  val K = 12
  val profits = List(5, 8, 14, 6, 13, 17, 10, 4)
  val weights = List(2, 3, 5, 2, 4, 6, 3, 1)

  type Choice = Boolean
  type State = (Int, Int) // L'index de la pièce suivante à évaluer, le poids maximal courant autorisé

  override def choices(s: State): Set[Choice] = {
    val w_after = s._2 + weights(s._1)
    if (w_after > K) Set(false) else Set(true, false)
  }

  override val periods = weights.length
  override val initialState: State = (0, 0)
  override val initialValuation: Int = 0
  override def transition(s: State, c: Choice): State = if (c) (s._1 + 1, s._2 + weights(s._1)) else (s._1 + 1, s._2)
  override def valuation(previousValuation: Int, previousState: State, c: Choice, ns: State): Int = previousValuation + (if (c) profits(previousState._1) else 0)
  override def isBetter(before: Int, after: Int): Boolean = after > before
  override def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[State] = {
    if (periodNr == periods) {
      val bestEntry = row.maxBy(_._2)
      Some(bestEntry._1)
    } else {
      None
    }
  }


  def main(args: Array[String]) {
    val (result, choices) = solve()

    val objectNames = 0.until(weights.size).map(x => ('A' + x).toChar)
    val formattedChoices = choices.zipWithIndex
      .filter { case (b, i) => b }
      .map { case (b, i) => objectNames(i) }
      .mkString(", ")

    println(s"Best valuation ${result} with choices: ${formattedChoices}")
  }

}
