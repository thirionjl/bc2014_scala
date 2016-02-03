package com.sopra.bc2014.xx;

import static com.sopra.bc2014.xx.OutputUtils.printout;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

public class CodeBreaker2 {

    static class Histogram {

        static <T> List<Map.Entry<T, Long>> getFrequencyHistogram(Stream<T> stream) {
            Map<T, Long> stats = stream.collect(groupingBy(identity(), counting()));
            Comparator<Entry<T, Long>> comparingValue = comparing(Map.Entry::getValue);
            return stats.entrySet().stream().sorted(comparingValue.reversed()).collect(toList());
        }

    }

    static class CharacterCodeDistance {

        private static int[] get(String cipher, String clear) {
            Validate.notBlank(cipher);
            Validate.notBlank(clear);
            Validate.isTrue(cipher.length() == clear.length(), "Both arguments should have same length");

            int[] distances = new int[cipher.length()];
            for (int i = 0; i < cipher.length(); i++) {
                char ci = cipher.charAt(i);
                char cl = clear.charAt(i);
                distances[i] = get(ci, cl);
            }
            return distances;
        }

        private static int get(char ciphered, char cleared) {
            int delta = ciphered - cleared;
            return delta < 0 ? 26 + delta : delta;
        }
    }

    static class Decoder {

        final int[] key;
        final int[] repeatedKey;
        final int[] ciphered;

        Decoder(IntStream cipheredChars, int[] key) {
            this.key = key;
            this.ciphered = cipheredChars.toArray();
            this.repeatedKey = repeatKey(key, ciphered.length);
        }

        Decoder(String ciphered, int[] key) {
            this(ciphered.chars(), key);
        }

        /* Repeats key as long as we need */
        int[] repeatKey(int[] key, int textLength) {
            return IntStream.iterate(0, p -> (p + 1) % key.length).map(idx -> key[idx]).limit(textLength).toArray();
        }

        String decode() {
            char[] clearBlock = new char[repeatedKey.length];
            int pos = 0;
            int idx = 0;
            for (Integer ci : ciphered) {
                int cl = 0;
                if (Character.isLetter(ci)) {
                    cl = decodeChar(ci, repeatedKey[pos]);
                    clearBlock[idx++] = (char) cl;
                    pos++;
                } else {
                    clearBlock[idx++] = (char) ci.intValue();
                }
            }
            return new String(clearBlock);
        }

        char decodeChar(int ci, int shift) {
            int cl = ci - shift;
            if ('a' <= ci && ci <= 'z' && cl < 'a') {
                cl += 26;
            }
            if ('A' <= ci && ci <= 'Z' && cl < 'A') {
                cl += 26;
            }
            return (char) cl;
        }
    }

    public static void main(String[] args) throws Exception {
        CipherText text = new CipherText("/textCypher2.txt");

        printout("\n=== Step 1: Analyze letter statistics ===");
        Predicate<Character> onlyLowerCaseCaracters = c -> Character.isLetter(c) && Character.isLowerCase(c);
        Stream<Character> characters = text.getCharacters(onlyLowerCaseCaracters);
        printout("Top 10 most frequent characters");
        printout(Histogram.getFrequencyHistogram(characters).subList(0, 10));
        printout("They are all about the same. => Cannot be a mono-alphabetic substitution");

        printout("\n=== Step 2: Analyze word statistics ===");
        Stream<String> words = text.getWords();
        printout("Top 10 most frequent words");
        printout(Histogram.getFrequencyHistogram(words).subList(0, 10));
        printout(
            "Some ciphered word are very frequent. It means that a same word is ciphered the same way multiple times, which indicates a block cipher");
        printout(
            "The most frequent word in english is (by far) 'the' and 7 most frequent words are 3 chars length. They could all be different encryption of 'the'");

        printout("\n=== Step 3: Compare character distances between 'the' and the highest 3-letter words ===");
        // vvh = the => 2 14 3
        printout("Get distances between vvh and the");
        int[] vvh = CharacterCodeDistance.get("vvh", "the");
        printout(Arrays.toString(vvh));

        // xie = the => 4 1 0
        printout("Get distances between xie and the");
        int[] xie = CharacterCodeDistance.get("xie", "the");
        printout(Arrays.toString(xie));

        // mjs = the => 19 2 14
        printout("Get distances between mjs and the");
        int[] mjs = CharacterCodeDistance.get("mjs", "the");
        printout(Arrays.toString(mjs));

        // tag = the => 0 19 2
        printout("Get distances between tag and the");
        int[] tag = CharacterCodeDistance.get("tag", "the");
        printout(Arrays.toString(tag));

        // uhx = the => 1 0 19
        printout("Get distances between uhx and the");
        int[] uhx = CharacterCodeDistance.get("uhx", "the");
        printout(Arrays.toString(uhx));

        // wlf = the => 3 4 1
        printout("Get distances between wlf and the");
        int[] wlf = CharacterCodeDistance.get("wlf", "the");
        printout(Arrays.toString(wlf));

        printout(
            "Arrays have a lot of subsequences in common. Arrays are like connected together by the chain  0 19 2 14 3 4 1 (0 19)");
        printout("=> Key is of length 7 and uses chain 0 19 2 14 3 4 1");

        printout(
            "\n=== Step 4: To find the correct permutation of 0 19 2 14 3 4 1 try decoding first word with all possibilities ===");
        printout(new Decoder("Toyvkdvf", new int[] {0, 19, 2, 14, 3, 4, 1}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {19, 2, 14, 3, 4, 1, 0}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {2, 14, 3, 4, 1, 0, 19}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {14, 3, 4, 1, 0, 19, 2}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {3, 4, 1, 0, 19, 2, 14}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {4, 1, 0, 19, 2, 14, 3}).decode());
        printout(new Decoder("Toyvkdvf", new int[] {1, 0, 19, 2, 14, 3, 4}).decode());
        printout("==> Only 1 0 19 2 14 3 4 gives an english word. That's our key!\n");

        printout("=== Step 5: Decode ===");
        printout(new Decoder(text.getCharacterCodes(), new int[] {1, 0, 19, 2, 14, 3, 4}).decode());
    }
}