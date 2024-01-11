package _2022;

import com.google.common.primitives.Chars;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;

public class Day3 {

    public static void main(String[] args) {
        var lines = Util.readFileToLines();
        int sum1 = 0, sum2 = 0;

        for (var line : lines) {
            var half = line.length() / 2;
            sum1 += findCommonPriority(line.substring(0, half), line.substring(half));
        }

        for (int i = 0; i < lines.size() - 2; i += 3) {
            sum2 += findCommonPriority(lines.get(i), lines.get(i + 1), lines.get(i + 2));
        }

        System.err.println("Part 1: " + sum1);
        System.err.println("Part 2: " + sum2);
    }

    private static int findCommonPriority(String... strings) {
        var common = new ArrayList<>(Chars.asList(strings[0].toCharArray()));
        Arrays.stream(strings, 1, strings.length)
            .forEach(s -> common.retainAll(Chars.asList(s.toCharArray())));
        var ch = common.get(0);
        return ch >= 97 ? (ch - 97 + 1) : (ch - 65 + 27);
    }

}
