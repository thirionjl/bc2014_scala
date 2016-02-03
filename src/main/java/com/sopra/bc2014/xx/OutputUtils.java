package com.sopra.bc2014.xx;

import com.google.common.base.Splitter;

public class OutputUtils {

    private static final int MAX_OUTPUT_LENGTH = 1200;
    private static final int MAX_LINE_LENGTH = 120;

    private static final String ELLIPSIS_FORMAT = "(... %d chars ommitted ... )";

    public static void printout(Object o) {
        String s = o == null ? "null" : o.toString();
        int overflow = s.length() - MAX_OUTPUT_LENGTH;

        if (overflow > 0) {
            s = s.substring(0, MAX_OUTPUT_LENGTH);
        }

        System.out.println(String.join("\n",
            Splitter.fixedLength(MAX_LINE_LENGTH).split(s)));

        if (overflow > 0) {
            System.out.println(String.format(ELLIPSIS_FORMAT, overflow));
        }
    }
}