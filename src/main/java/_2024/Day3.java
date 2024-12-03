package _2024;

import util.Util;

import java.util.TreeMap;
import java.util.regex.Pattern;

public class Day3 {

    static Pattern MUL_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    static Pattern DO_PATTERN = Pattern.compile("do\\(\\)");
    static Pattern DONT_PATTERN = Pattern.compile("don't\\(\\)");

    public static void main(String[] args) {
        var input = String.join("", Util.readFileToLines());
        System.err.println("Part 1: " + calSum1(input));
        System.err.println("Part 2: " + calSum2(input));
    }

    private static int calSum1(String input) {
        var matcher = MUL_PATTERN.matcher(input);
        var sum = 0;
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            sum += x * y;
        }
        return sum;
    }

    public static int calSum2(String input) {
        TreeMap<Integer, Boolean> instructionMap = new TreeMap<>();
        var doMatcher = DO_PATTERN.matcher(input);
        while (doMatcher.find()) {
            instructionMap.put(doMatcher.start(), true);
        }
        var dontMatcher = DONT_PATTERN.matcher(input);
        while (dontMatcher.find()) {
            instructionMap.put(dontMatcher.start(), false);
        }
        // Set do at start
        instructionMap.putIfAbsent(-1, true);

        var sum = 0;
        var mulMatcher = MUL_PATTERN.matcher(input);
        while (mulMatcher.find()) {
            var mulIndex = mulMatcher.start();
            var enabled = instructionMap.floorEntry(mulIndex).getValue();
            if (enabled) {
                int x = Integer.parseInt(mulMatcher.group(1));
                int y = Integer.parseInt(mulMatcher.group(2));
                sum += x * y;
            }
        }

        return sum;
    }

}
