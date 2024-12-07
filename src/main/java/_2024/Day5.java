package _2024;

import util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 {

    private static Map<Integer, Set<Integer>> graph;

    public static void main(String[] args) {
        var input = Util.readFileToBlocks();
        graph = input.getFirst().stream()
            .map(line -> line.split("\\|"))
            .collect(Collectors.groupingBy(
                parts -> Integer.parseInt(parts[0]),
                Collectors.mapping(parts -> Integer.parseInt(parts[1]), Collectors.toSet())
            ));
        var updates = input.get(1).stream()
            .map(line -> Util.parseLine(line, ",", Integer::parseInt))
            .toList();

        var part1 = updates.stream()
            .filter(Day5::isValidOrder)
            .mapToInt(update -> update.get(update.size() / 2))
            .sum();
        System.err.println("Part 1: " + part1);

        var part2 = updates.stream()
            .filter(update -> !Day5.isValidOrder(update))
            .map(Day5::fixToValid)
            .mapToInt(update -> update.get(update.size() / 2))
            .sum();
        System.err.println("Part 2: " + part2);
    }

    private static boolean isValidOrder(List<Integer> pages) {
        return IntStream.range(0, pages.size())
            .allMatch(i -> IntStream.range(i + 1, pages.size())
                .noneMatch(j -> graph.get(pages.get(j)).contains(pages.get(i))));
    }

    private static List<Integer> fixToValid(List<Integer> pages) {
        var workingPages = new ArrayList<>(pages);
        while (!isValidOrder(workingPages)) findAndSwap(workingPages);
        return workingPages;
    }

    private static void findAndSwap(ArrayList<Integer> workingPages) {
        for (int i = 0; i < workingPages.size() - 1; i++) {
            for (int j = i + 1; j < workingPages.size(); j++) {
                if (graph.get(workingPages.get(j)).contains(workingPages.get(i))) {
                    Collections.swap(workingPages, i, j);
                    return;
                }
            }
        }
    }

}
