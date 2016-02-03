package com.sopra.bc2014

import org.apache.commons.lang3.StringUtils

import scala.io.Source._

object CipherText {
  val WORD_SEPARATORS = "[^a-zA-Z]"
}

case class CipherText(path: String) {

  import CipherText._

  def getLines(): Iterator[String] = {
    fromInputStream(getClass.getResourceAsStream(path), "ISO-8859-1").getLines()
  }

  def getWords(): Iterator[String] = {
    getLines().flatMap(_.split(WORD_SEPARATORS)).filter(StringUtils.isNotBlank(_))
  }

  def getCharacters(f: Char => Boolean = x => true): Iterator[Char] = {
    fromInputStream(getClass.getResourceAsStream(path), "ISO-8859-1").filter(f)
  }

  def getCharacterCodes(f: Char => Boolean = x => true): Iterator[Int] = {
    getCharacters(f).map(c => c.charValue().toInt)
  }

}
