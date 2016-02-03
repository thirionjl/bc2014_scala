package com.sopra.bc2014.dp

import scala.collection._

trait DynamicProgramming {
  val periods: Int

  type State
  type Choice
  def choices(s: State): Set[Choice]
  def transition(s: State, c: Choice): State
  def valuation(previousValuation: Int, previousState: State, c: Choice, newState: State): Int
  def isBetter(previousValuation: Int, newValuation: Int): Boolean

  val initialState: State
  val initialValuation: Int
  def findTargetState(periodNr: Int, row: Iterator[(State, Int)]): Option[State]

  def nextStates(states: mutable.Map[State, (Int, List[Choice])], periodNr: Int): (mutable.Map[State, (Int, List[Choice])], Boolean) = {
    println(s"Computing state from: $periodNr")
    var result: mutable.Map[State, (Int, List[Choice])] = mutable.Map()
    for ((s, (v, previousChoices)) <- states) {
      for (c <- choices(s)) {
        val newState = transition(s, c)
        val newValuation = valuation(v, s, c, newState)
        val oldValuation = result.get(newState)
        if (oldValuation.isEmpty || isBetter(oldValuation.get._1, newValuation)) {
          result += newState ->(newValuation, c :: previousChoices)
        }
      }
    }

    val targetState: Option[State] = findTargetState(periodNr, result.iterator.map { case (state, (value, _)) => (state, value) })

    if (targetState.isDefined) {
      val ts = targetState.get
      result = mutable.Map(ts -> result(ts))
    }
    (result, targetState.isDefined)
  }

  def solve(): (Int, List[Choice]) = {
    var states: mutable.Map[State, (Int, List[Choice])] = mutable.Map(initialState ->(initialValuation, List()))
    var i = 0
    var targetStateFound = false
    while (i < periods && !targetStateFound) {
      val rb = nextStates(states, i + 1)
      states = rb._1
      targetStateFound = rb._2
      i += 1
    }

    if (states.size != 1) sys.error("There should be only one target state")
    val (topValuation, reversedChoices) = states.values.head
    (topValuation, reversedChoices.reverse)
  }
}


