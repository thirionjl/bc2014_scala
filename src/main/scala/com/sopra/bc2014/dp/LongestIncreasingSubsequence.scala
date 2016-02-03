package com.sopra.bc2014.dp

import scala.collection.Set
import scala.math._

object LongestIncreasingSubsequence extends DynamicProgramming {

  val sequence = List(555, 3, 4, 7, 100, 99, 5, 1)

  override val periods: Int = sequence.length
  override type Choice = Int
  // Valeur à évaluer
  override type State = (Int, Int, Int) // Index suivant à évaluer, Taille de la sous-séquence en cours à la fin de l'étape i, Dernière valeur de la sous-séquence en cours à la fin de l'étape i

  override val initialValuation: Int = 0
  override val initialState: State = (0, 0, Int.MinValue)
  override def choices(s: State): Set[Choice] = Set(sequence(s._1))
  override def transition(s: State, c: Choice): State = if (c >= s._3) (s._1 + 1, s._2 + 1, c) else (s._1 + 1, 1, c)
  override def valuation(previousValuation: Int, previousState: State, c: Choice, ns: State): Int = max(ns._2, previousValuation)
  override def isBetter(previousValuation: Int, newValuation: Int): Boolean = newValuation > previousValuation
  override def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[State] = {
    if (periodNr == periods) {
      Some(row.next()._1)
    } else {
      None
    }
  }

  def main(args: Array[String]) {
    println(solve()._1)
  }
}
