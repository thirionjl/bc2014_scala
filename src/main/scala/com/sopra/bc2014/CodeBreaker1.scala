package com.sopra.bc2014

import com.sopra.bc2014.util.TraversableOnceOps.TraversableIntOps

object CodeBreaker1 {

  val MOST_FREQUENT_ENGLISH_WORD = "the"

  def looksLikeEnglish(s: String): Boolean = s.contains(MOST_FREQUENT_ENGLISH_WORD)

  def shiftCharacterCodes(text: CipherText, shift: Int): String = {
    val decodeChars = text.getCharacterCodes().map(_ - shift).toArray
    new String(decodeChars.map(_.toChar))
  }

  def main(args: Array[String]) {
    val text = CipherText("/textCypher1.txt")

    // With such a short text impossible to get enough redundancy. Sole
    // solution is a Cesar cipher (shifting character codes)
    val cipherTextAverageChar = text.getCharacterCodes().average().toInt
    println(s">>> Ciphertext text average char code: " + cipherTextAverageChar)
    val englishTextAverageChar = 'e'.toInt
    println(">>> English text average char code: " + englishTextAverageChar)
    val averageDistance = cipherTextAverageChar - englishTextAverageChar
    println(f">>> Average character distance is $averageDistance%d")

    println(">>> Decode by trying all possible shifts")

    ((averageDistance - 10) to (averageDistance + 10))
      .map(shiftCharacterCodes(text, _))
      .find(looksLikeEnglish)
      .foreach(println)
  }


}
