package _2022;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {

    static Pattern valvePattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnel(?:s)? lead(?:s)? to valve(?:s)? ([\\w, ]+)?");
    static Map<String, Valve> valveMap;
    static Map<String, Map<String, Integer>> shortestPaths = new HashMap<>();
    static List<String> flowValves;

    @Data
    @AllArgsConstructor
    static class Valve {
        String name;
        int rate;
        List<String> neighbors;
    }

    @Data
    @AllArgsConstructor
    static class State {
        String currentValve;
        int currentTime;
        Set<String> opened;
        Set<Pair<String, Integer>> openedWithTime;
    }

    public static void main(String[] args) {
        valveMap = parseInput();
        valveMap.keySet().forEach(valve -> shortestPaths.put(valve, findShortestPathFrom(valve)));
        flowValves = valveMap.values().stream()
            .filter(valve -> valve.rate > 0)
            .map(Valve::getName)
            .toList();

        var maxHuman = generateMaxFlowMap(30).values().stream()
            .max(Comparator.naturalOrder())
            .get();
        System.err.println("Part 1: " + maxHuman);

        var maxFlowWithElephant = findMaxFlowWithElephant(26);
        System.err.println("Part 2: " + maxFlowWithElephant);
    }

    private static int findMaxFlowWithElephant(int totalTime) {
        var flowMap = generateMaxFlowMap(totalTime);
        var maxPressure = 0;

        for (var humanFlow : flowMap.entrySet()) {
            for (var elephantFlow : flowMap.entrySet()) {
                if (Collections.disjoint(humanFlow.getKey(), elephantFlow.getKey())) {
                    maxPressure = Math.max(maxPressure, humanFlow.getValue() + elephantFlow.getValue());
                }
            }
        }

        return maxPressure;
    }

    private static Map<Set<String>, Integer> generateMaxFlowMap(int totalTime) {
        Map<Set<String>, Integer> maxFlowMap = new HashMap<>();
        Stack<State> stack = new Stack<>();
        stack.push(new State("AA", 0, Set.of(), Set.of()));

        while (!stack.isEmpty()) {
            var currentState = stack.pop();
            if (currentState.currentTime <= totalTime) {
                var maxFlow = calculateFlow(currentState.openedWithTime, totalTime);
                maxFlowMap.compute(currentState.opened, (k, v) -> (v == null) ? maxFlow : Math.max(maxFlow, v));
            }

            for (var nextValve : flowValves) {
                if (!currentState.opened.contains(nextValve)) {
                    var newTime = currentState.currentTime + shortestPaths.get(currentState.currentValve).get(nextValve) + 1;
                    if (newTime <= totalTime) {
                        var opened = new HashSet<>(currentState.opened);
                        opened.add(nextValve);
                        var openedWithTime = new HashSet<>(currentState.openedWithTime);
                        openedWithTime.add(Pair.of(nextValve, newTime));
                        stack.push(new State(nextValve, newTime, opened, openedWithTime));
                    }
                }
            }
        }

        return maxFlowMap;
    }

    private static int calculateFlow(Set<Pair<String, Integer>> path, int totalTime) {
        return path.stream()
            .map(p -> (totalTime - p.getRight()) * valveMap.get(p.getLeft()).rate)
            .reduce(0, Integer::sum);
    }

    private static Map<String, Integer> findShortestPathFrom(String valve) {
        Map<String, Integer> minTurns = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        valveMap.keySet().forEach(v -> minTurns.put(v, Integer.MAX_VALUE));
        minTurns.put(valve, 0);
        queue.offer(valve);

        while (!queue.isEmpty()) {
            var currentValve = queue.poll();
            var turns = minTurns.get(currentValve);

            for (var neighbor : valveMap.get(currentValve).neighbors) {
                if (turns + 1 < minTurns.get(neighbor)) {
                    queue.offer(neighbor);
                    minTurns.put(neighbor, turns + 1);
                }
            }
        }

        return minTurns;
    }

    private static Map<String, Valve> parseInput() {
        return Util.readFileToLines().stream()
            .map(line -> {
                var matcher = valvePattern.matcher(line);
                matcher.find();
                var name = matcher.group(1);
                var rate = Integer.parseInt(matcher.group(2));
                var neighbors = Arrays.asList(matcher.group(3).split(",\\s*"));
                return new Valve(name, rate, neighbors);
            })
            .collect(Collectors.toMap(v -> v.name, Function.identity()));
    }

}