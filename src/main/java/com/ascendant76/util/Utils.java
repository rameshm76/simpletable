package com.ascendant76.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public final class Utils {

    private Utils() {
        // private constructor
    }

    public static String ifNull(String val, String defaultVal) {
        String result = defaultVal;
        if (null != val) {
            result = val;
        }

        return result;
    }

    public static String capitalize(String line) {
        Spliterator<String> tokens = Splitter.on(' ').omitEmptyStrings().split(line).spliterator();
        Stream<String> capitalizedTokens = StreamSupport.stream(tokens, false).map(input -> Character.toUpperCase(input.charAt(0)) + input.substring(1));
        return Joiner.on(' ').join(capitalizedTokens.toArray());
    }
}
