package com.sopra.bc2014

import org.apache.commons.lang3.Validate

object CodeBreaker2 {

  object Histogram {
    def getFrequencyHistogram[T](it: Iterable[T]): Seq[(T, Int)] = {
      val stats: Seq[(T, Int)] = it.groupBy(x => x).mapValues(_.size).toSeq
      stats.sortBy(-_._2)
    }
  }

  object CharacterCodeDistance {

    def get(cipher: String, clear: String): Seq[Int] = {
      Validate.notBlank(cipher)
      Validate.notBlank(clear)
      Validate.isTrue(cipher.length == clear.length, "Both arguments should have same length")
      cipher.zip(clear).map { case (ci, cl) => get(ci, cl) }
    }

    def get(ciphered: Char, cleared: Char): Int = {
      val delta: Int = ciphered - cleared
      if (delta < 0) 26 + delta else delta
    }

  }

  def repeatKey(key: Array[Int], textLength: Int): Array[Int] = {
    Iterator.iterate[Int](0)(p => (p + 1) % key.length).map(key(_)).take(textLength).toArray
  }

  class Decoder(val key: Array[Int], val repeatedKey: Array[Int], val ciphered: Array[Int]) {

    def this(cipheredChars: Array[Int], key: Array[Int]) {
      this(key, repeatKey(key, cipheredChars.length), cipheredChars.toArray)
    }

    def this(ciphered: String, key: Array[Int]) {
      this(ciphered.iterator.map(_.toInt).toArray, key)
    }

    def decode(): String = {
      val (clear, _) = ciphered.foldLeft[(List[Char], Int)]((Nil, 0)) {
        case ((decoded, pos), ci) =>
          if (Character.isLetter(ci))
            (decodeChar(ci, repeatedKey(pos)) :: decoded, pos + 1)
          else
            (ci.toChar :: decoded, pos)
      }
      new String(clear.reverse.toArray)
    }

    def decodeChar(ci: Int, shift: Int): Char = {
      var cl: Int = ci - shift
      if (('a' <= ci && ci <= 'z' && cl < 'a') || ('A' <= ci && ci <= 'Z' && cl < 'A')) {
        cl += 26
      }
      cl.toChar
    }
  }

  def main(args: Array[String]) {
    val text: CipherText = new CipherText("/textCypher2.txt")

    println("\n=== Step 1: Analyze letter statistics ===")
    val onlyLowerCaseCaracters: Char => Boolean = c => Character.isLetter(c) && Character.isLowerCase(c)
    val characters: Iterator[Char] = text.getCharacters(onlyLowerCaseCaracters)
    println("Top 10 most frequent characters")
    println(Histogram.getFrequencyHistogram(characters.toSeq).take(10))
    println("They are all about the same. => Cannot be a mono-alphabetic substitution")

    println("\n=== Step 2: Analyze word statistics ===")
    val words: Iterator[String] = text.getWords
    println("Top 10 most frequent words")
    println(Histogram.getFrequencyHistogram(words.toSeq).take(10))
    println(
      "Some ciphered word are very frequent. It means that a same word is ciphered the same way multiple times, which indicates a block cipher")
    println(
      "The most frequent word in english is (by far) 'the' and 7 most frequent words are 3 chars length. They could all be different encryption of 'the'")

    println("\n=== Step 3: Compare character distances between 'the' and the highest 3-letter words ===")
    // vvh = the => 2 14 3
    println("Get distances between vvh and the")
    val vvh: Seq[Int] = CharacterCodeDistance.get("vvh", "the")
    println(vvh)

    // xie = the => 4 1 0
    println("Get distances between xie and the")
    val xie: Seq[Int] = CharacterCodeDistance.get("xie", "the")
    println(xie)

    // mjs = the => 19 2 14
    println("Get distances between mjs and the")
    val mjs: Seq[Int] = CharacterCodeDistance.get("mjs", "the")
    println(mjs)

    // tag = the => 0 19 2
    println("Get distances between tag and the")
    val tag: Seq[Int] = CharacterCodeDistance.get("tag", "the")
    println(tag)

    // uhx = the => 1 0 19
    println("Get distances between uhx and the")
    val uhx: Seq[Int] = CharacterCodeDistance.get("uhx", "the")
    println(uhx)

    // wlf = the => 3 4 1
    println("Get distances between wlf and the")
    val wlf: Seq[Int] = CharacterCodeDistance.get("wlf", "the")
    println(wlf)

    println(
      "Arrays have a lot of subsequences in common. Arrays are like connected together by the chain  0 19 2 14 3 4 1 (0 19)")
    println("=> Key is of length 7 and uses chain 0 19 2 14 3 4 1")

    println("\n=== Step 4: To find the correct permutation of 0 19 2 14 3 4 1 try decoding first word with all possibilities ===")
    println(new Decoder("Toyvkdvf", Array(0, 19, 2, 14, 3, 4, 1)).decode)
    println(new Decoder("Toyvkdvf", Array(19, 2, 14, 3, 4, 1, 0)).decode)
    println(new Decoder("Toyvkdvf", Array(2, 14, 3, 4, 1, 0, 19)).decode)
    println(new Decoder("Toyvkdvf", Array(14, 3, 4, 1, 0, 19, 2)).decode)
    println(new Decoder("Toyvkdvf", Array(3, 4, 1, 0, 19, 2, 14)).decode)
    println(new Decoder("Toyvkdvf", Array(4, 1, 0, 19, 2, 14, 3)).decode)
    println(new Decoder("Toyvkdvf", Array(1, 0, 19, 2, 14, 3, 4)).decode)
    println("==> Only 1 0 19 2 14 3 4 gives an english word. That's our key!\n")

    println("=== Step 5: Decode ===")
    println(new Decoder(text.getCharacterCodes().toArray, Array(1, 0, 19, 2, 14, 3, 4)).decode)
  }

}
