package _2024;

import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day2 {

    public static void main(String[] args) {
        var reports = Util.readFileToLines().stream()
            .map(rp -> Util.parseLine(rp, "\\s+", Integer::parseInt))
            .toList();

        var part1 = reports.stream().filter(Day2::isSafe1).count();
        System.err.println("Part 1: " + part1);

        var part2 = reports.stream().filter(Day2::isSafe2).count();
        System.err.println("Part 2: " + part2);
    }

    private static boolean isSafe2(List<Integer> levels) {
        return isSafe1(levels) ||
            IntStream.range(0, levels.size())
                .anyMatch(i -> {
                    var removed = new ArrayList<>(levels);
                    removed.remove(i);
                    return isSafe1(removed);
                });
    }

    private static boolean isSafe1(List<Integer> levels) {
        var direction = Math.signum(levels.get(0) - levels.get(1));
        return IntStream.range(0, levels.size() - 1)
            .allMatch(i -> {
                var diff = levels.get(i) - levels.get(i + 1);
                return Math.signum(diff) == direction &&
                    Math.abs(diff) >= 1 &&
                    Math.abs(diff) <= 3;
            });
    }

}
