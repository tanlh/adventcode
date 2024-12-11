package _2024;

import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 {

    private static final long MULTIPLIER = 2024;
    private static final Map<Long, List<Long>> memo = new HashMap<>();

    public static void main(String[] args) {
        var stones = Util.parseLine(Util.readFileToLines().getFirst(), "\\s+", Long::parseLong)
            .stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var blinkTimes = 75;

        var totalStones = (Long) Stream.iterate(stones, counts -> counts.entrySet()
                .stream()
                .flatMap(e -> blink(e.getKey()).stream().map(n -> Map.entry(n, e.getValue())))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue))))
            .limit(blinkTimes + 1)
            .reduce((_, last) -> last)
            .orElseThrow().values().stream().mapToLong(Long::longValue).sum();
        System.err.println("Result: " + totalStones);
    }

    private static List<Long> blink(long stone) {
        return memo.computeIfAbsent(stone, n -> {
            if (n == 0) return List.of(1L);
            var length = (int) Math.floor(Math.log10(n)) + 1;
            if (length % 2 == 0) {
                var pow = (long) Math.pow(10, (double) length / 2);
                return List.of(n / pow, n % pow);
            }
            return List.of(n * MULTIPLIER);
        });
    }

}
