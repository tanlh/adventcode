package _2024;

import util.Util;

import java.util.*;

public class Day23 {

    private static final Map<String, Set<String>> graph = new HashMap<>();
    private static List<String> nodes;
    private static Set<String> largestGroup = new HashSet<>();

    public static void main(String[] args) {
        buildGraph();
        findLargestGroup(new HashSet<>(), 0);

        System.err.println("Part 1: " + countTriangleHasChief());
        System.err.println("Part 2: " + String.join(",", largestGroup.stream().sorted().toList()));
    }

    private static void buildGraph() {
        Util.readFileToLines().forEach(connection -> {
            var parts = connection.split("-");
            var from = parts[0];
            var to = parts[1];
            graph.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            graph.computeIfAbsent(to, k -> new HashSet<>()).add(from);
        });
        nodes = new ArrayList<>(graph.keySet());
    }

    private static long countTriangleHasChief() {
        return graph.keySet().stream()
            .flatMap(node1 -> graph.get(node1).stream()
                .flatMap(node2 -> graph.get(node1).stream()
                    .filter(node3 -> graph.get(node2).contains(node3))
                    .map(node3 -> Set.of(node1, node2, node3))))
            .distinct()
            .filter(triangle -> triangle.stream().anyMatch(n -> n.startsWith("t")))
            .count();
    }

    private static void findLargestGroup(Set<String> group, int start) {
        if (group.size() > largestGroup.size()) largestGroup = new HashSet<>(group);

        nodes.subList(start, nodes.size()).stream()
            .filter(node -> graph.get(node).containsAll(group))
            .forEach(node -> {
                group.add(node);
                findLargestGroup(group, nodes.indexOf(node) + 1);
                group.remove(node);
            });
    }

}
