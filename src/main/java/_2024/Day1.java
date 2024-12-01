package _2024;

import util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day1 {

    public static void main(String[] args) {
        List<Long> left = new ArrayList<>();
        List<Long> right = new ArrayList<>();
        Util.readFileToLines()
            .forEach(line -> {
                var parts = line.split("\\s+");
                left.add(Long.parseLong(parts[0]));
                right.add(Long.parseLong(parts[1]));
            });

        // Part 1
        left.sort(Comparator.naturalOrder());
        right.sort(Comparator.naturalOrder());
        var part1 = IntStream.range(0, left.size())
            .mapToLong(i -> Math.abs(left.get(i) - right.get(i)))
            .sum();
        System.err.println("Part 1: " + part1);

        // Part 2
        var map = right.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var part2 = left.stream()
            .mapToLong(val -> val * map.getOrDefault(val, 0L))
            .sum();
        System.err.println("Part 2: " + part2);
    }

}
