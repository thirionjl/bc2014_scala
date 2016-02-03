package com.sopra.bc2014.dp

import scala.collection.Set

object Partition extends DynamicProgramming {

  override type Choice = Boolean
  // Intégrer ou pas la pièce courante dans le sac
  override type State = (Int, Int) // La prochaine pièce à évaluer, et la somme des pièces dans le sac

  val bag = List(5, 9, 3, 8, 2, 5)
  val maxSum = {
    val sum = bag.sum
    if (sum % 2 == 0) sum / 2 else (sum - 1) / 2
  }

  override val periods: Int = bag.size
  override val initialState: State = (0, 0)
  override val initialValuation: Int = 0


  override def choices(s: State): Set[Choice] = {
    if (bag(s._1) + s._2 <= maxSum) Set(true, false) else Set(false)
  }

  override def transition(s: State, c: Choice): State = {
    val added = if (c) bag(s._1) else 0
    (s._1 + 1, s._2 + added)
  }

  override def valuation(previousValuation: Int, previousState: State, c: Choice, newState: State): Int = newState._2
  override def isBetter(previousValuation: Int, newValuation: Int): Boolean = newValuation > previousValuation
  override def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[State] = {
    val (highestValuedState, highestValuation) = row.maxBy { case ((i, sum), value) => value }
    if (highestValuation == maxSum || periodNr == periods) Some(highestValuedState)
    else None
  }

  def main(args: Array[String]) {
    val (result, choices) = solve()

    val formattedChoices = choices.zipWithIndex
      .filter { case (b, i) => b }
      .map { case (b, i) => bag(i) }
      .mkString(", ")

    println(s"Best valuation ${result} with choices: ${formattedChoices}")
  }

}
