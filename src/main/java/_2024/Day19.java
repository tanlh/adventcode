package _2024;

import util.Util;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 {

    public static void main(String[] args) {
        var input = Util.readFileToBlocks();
        var patterns = Set.copyOf(Util.parseLine(input.getFirst().getFirst(), ", ", Function.identity()));

        var result = input.getLast().stream()
            .map(design -> countArrangement(patterns, design))
            .filter(n -> n > 0)
            .collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.summingLong(Long::longValue),
                (count, sum) -> new long[]{count, sum}
            ));
        System.err.println("Part 1: " + result[0]);
        System.err.println("Part 2: " + result[1]);
    }

    private static long countArrangement(Set<String> patterns, String design) {
        var n = design.length();
        var dp = new long[n + 1];
        dp[0] = 1;

        IntStream.rangeClosed(1, n).forEach(i -> IntStream.rangeClosed(1, i)
            .filter(j -> patterns.contains(design.substring(j - 1, i)))
            .forEach(j -> dp[i] += dp[j - 1]));
        return dp[n];
    }

}
