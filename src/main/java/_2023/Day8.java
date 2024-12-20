package _2023;

import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.util.*;

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
            .toList();
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

        // Actually each start node end with just one Z node. The zMap only contains one key :)))
        // This method is to handle in case end with many Z node
        var minSteps = Util.generateCombinations(zStepList).stream()
            .map(Util::findLCM)
            .min(Comparator.naturalOrder())
            .get();
        // var minSteps = findLCM(List.of(20093, 12169, 22357, 14999, 13301, 17263));
        System.err.println(minSteps);
    }

}
