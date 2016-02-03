package com.sopra.bc2014.dp

object MoneyReturn extends DynamicProgramming {
  override type State = Int // La somme (+ l'étape)
  override type Choice = Int // Choix d'une pièce avec son montant

  val coins: Set[Int] = Set(0, 1, 2, 5, 10)
  val target = 24
  override val initialState: State = 0


  override val periods: Int = 31
  override def choices(s: State): Set[Choice] = coins.filter(s + _ <= target)
  override def transition(s: State, c: Choice): State = s + c
  override def valuation(previousValuation: Int, previousState: State, c: Choice, ns: State): Int = if (c == 0) previousValuation else previousValuation + 1
  override def isBetter(previousValuation: Int, newValuation: Int): Boolean = newValuation < previousValuation

  override def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[Int] = {
    val x: Option[(State, Int)] = row.find { case (state, _) => state == target }
    if (x.isDefined) Some(x.get._1) else None
  }


  override val initialValuation: Int = 0
  def main(args: Array[String]) {
    val (res, choices) = solve()
    println(choices.filter(_ > 0).groupBy(x => x).mapValues(_.size + " time(s)"))
  }
}
