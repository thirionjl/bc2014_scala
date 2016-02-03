package com.sopra.bc2014

import scala.io.Source._


object NumbersAndLetters {

  def getLongestWords(draw: String, dictionary: Iterator[String]): List[String] = {
    val len = (s: String) => s.replaceAll("-", "").length()
    val allMatches: List[String] = dictionary.filter(matches(draw, _)).toList
    val matchesPerSize: List[(Int, List[String])] = allMatches.groupBy(len).toList

    if (matchesPerSize.isEmpty) {
      Nil
    }
    else {
      val (_, longestWords) = matchesPerSize.maxBy(_._1)
      longestWords
    }
  }

  def matches(draw: String, word: String): Boolean = {
    val normalizedWord = for (rawChar <- word.toLowerCase().toCharArray() if !isIgnored(rawChar)) yield noAccent(rawChar)
    val (matchResult, _) = normalizedWord.iterator.foldRight((true, draw)) {
      case (c, (queueMatches, allowedChars)) =>
        val allMatches = contains(allowedChars, c) && queueMatches
        (allMatches, removeChar(allowedChars, c))
    }
    matchResult
  }

  def isIgnored(c: Char): Boolean = "-".indexOf(c) >= 0
  def contains(remainingChars: String, c: Char): Boolean = remainingChars.indexOf(c) >= 0
  def removeChar(remainingChars: String, usedChar: Char): String = remainingChars.replaceFirst(Character.toString(usedChar), "")

  def noAccent(c: Char): Char = {
    if ("äâä".indexOf(c) >= 0) 'a'
    else if ("ç".indexOf(c) >= 0) 'c'
    else if ("èéêë".indexOf(c) >= 0) 'e'
    else if ("îï".indexOf(c) >= 0) 'i'
    else if ("ôö".indexOf(c) >= 0) 'o'
    else if ("ùûü".indexOf(c) >= 0) 'u'
    else c
  }

  def dictionary(path: String): Iterator[String] = {
    fromInputStream(getClass.getResourceAsStream(path), "ISO-8859-1").getLines()
  }

  def main(args: Array[String]) {
    getLongestWords("smarinousz", dictionary("/dictionary.txt")) foreach (println(_))
  }

}
