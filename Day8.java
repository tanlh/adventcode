package com.alibaba.logistics.station;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) {
        var lines = Util.readFileToLines();
        var instructions = Util.splitLine(lines.get(0), "");
        lines.remove(0);

        Map<String, Pair<String, String>> map = new LinkedHashMap<>();
        lines.forEach(line -> {
            var parts = line.split("=");
            var key = parts[0].trim();
            var values = parts[1].replaceAll("[()]", "").split(",");
            map.put(key, Pair.of(values[0].trim(), values[1].trim()));
        });

        // [GNA, FCA, AAA, MXA, VVA, XHA]
        var startNodes = map.keySet().stream()
            .filter(node -> node.endsWith("A"))
            .collect(Collectors.toList());
        List<List<Integer>> zStepList = new ArrayList<>();

        for (var node : startNodes) {
            int steps = 0;
            var lastNode = node;
            Map<String, Integer> zMap = new HashMap<>();

            while (!zMap.containsKey(lastNode)) {
                for (var instruction : instructions) {
                    steps += 1;
                    var next = map.get(lastNode);
                    lastNode = "L".equals(instruction) ? next.getLeft() : next.getRight();

                    if (lastNode.endsWith("Z")) {
                        zMap.put(lastNode, steps);
                    }
                }
            }

            zStepList.add(new ArrayList<>(zMap.values()));
        }

        List<List<Integer>> combinations = new ArrayList<>();
        generateCombinations(zStepList, new ArrayList<>(), 0, combinations);

        var minSteps = combinations.stream()
            .map(Day8::findLCM)
            .min(Comparator.naturalOrder())
            .get();
        // var minSteps = findLCM(List.of(20093, 12169, 22357, 14999, 13301, 17263));
        System.err.println(minSteps);
    }

    // Actually each start node end with just one Z node. The zMap only contains one key :)))
    // This method is to handle in case end with many Z node
    private static void generateCombinations(List<List<Integer>> collections, List<Integer> currentCombination, int currentIndex, List<List<Integer>> combinations) {
        if (currentIndex == collections.size()) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        List<Integer> currentCollection = collections.get(currentIndex);
        for (Integer number : currentCollection) {
            currentCombination.add(number);
            generateCombinations(collections, currentCombination, currentIndex + 1, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    private static long findLCM(List<Integer> numbers) {
        long lcm = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            lcm = calculateLCM(lcm, numbers.get(i));
        }
        return lcm;
    }

    private static long calculateLCM(long a, long b) {
        return (a * b) / calculateGCD(a, b);
    }

    private static long calculateGCD(long a, long b) {
        return b == 0 ? a : calculateGCD(b, a % b);
    }

}
