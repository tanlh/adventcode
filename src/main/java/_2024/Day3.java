package _2024;

import util.Util;

import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day3 {

    static Pattern MUL_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    static Pattern DO_PATTERN = Pattern.compile("do\\(\\)");
    static Pattern DONT_PATTERN = Pattern.compile("don't\\(\\)");

    public static void main(String[] args) {
        var input = String.join("", Util.readFileToLines());

        // 189527826
        var part1 = calSum(input, m -> true);
        System.err.println("Part 1: " + part1);

        var instructionMap = new TreeMap<Integer, Boolean>();
        DO_PATTERN.matcher(input).results().forEach(match -> instructionMap.put(match.start(), true));
        DONT_PATTERN.matcher(input).results().forEach(match -> instructionMap.put(match.start(), false));
        instructionMap.putIfAbsent(-1, true);

        var part2 = calSum(input, m -> instructionMap.floorEntry(m.start()).getValue());
        System.err.println("Part 2: " + part2);
    }

    private static int calSum(String input, Predicate<MatchResult> checker) {
        return MUL_PATTERN.matcher(input)
            .results()
            .filter(checker)
            .mapToInt(match -> Integer.parseInt(match.group(1)) * Integer.parseInt(match.group(2)))
            .sum();
    }

}
