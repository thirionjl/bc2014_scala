package com.sopra.bc2014;

import java.util.LinkedList;
import java.util.List;

public class PythagoreJava {

    static List<String> solve(int x) {
        List<String> solutions = new LinkedList<>();
        int max = x / 2 + 1;
        for (int a = 0; a < max; a++) {
            for (int b = 0; b < max; b++) {
                for (int c = 0; c < max; c++) {
                    if (a + b + c == x && a * a + b * b == c * c) {
                        solutions.add(String.format("{a=%d b=%d c=%d}", a, b, c));
                    }
                }
            }
        }
        return solutions;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(String.join("\n", solve(5000)));
        long stop = System.currentTimeMillis();
        System.out.println("Elapsed ms:" + (stop - start));
    }

}
