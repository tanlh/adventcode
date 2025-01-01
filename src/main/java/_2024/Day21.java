package _2024;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.tuple.Pair;
import util.Point;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.entry;
import static util.Constants.DIRMOVEMAP;

public class Day21 {

    record Node(Point pos, String moves) {}

    private static final BiMap<Character, Point> NUM_KEYPAD = HashBiMap.create(Map.ofEntries(
        entry('7', new Point(0, 0)), entry('8', new Point(1, 0)), entry('9', new Point(2, 0)),
        entry('4', new Point(0, 1)), entry('5', new Point(1, 1)), entry('6', new Point(2, 1)),
        entry('1', new Point(0, 2)), entry('2', new Point(1, 2)), entry('3', new Point(2, 2)),
        entry('0', new Point(1, 3)), entry('A', new Point(2, 3))
    ));
    private static final Map<String, List<String>> NUM_PATHS = computePaths(NUM_KEYPAD);

    private static final BiMap<Character, Point> DIR_KEYPAD = HashBiMap.create(Map.ofEntries(
        entry('^', new Point(1, 0)), entry('A', new Point(2, 0)),
        entry('<', new Point(0, 1)), entry('v', new Point(1, 1)), entry('>', new Point(2, 1))
    ));

    private static final Map<String, List<String>> DIR_PATHS = computePaths(DIR_KEYPAD);
    private static final Map<String, Integer> DIR_LENGTHS = DIR_PATHS.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getFirst().length()));

    private static final Map<Pair<String, Integer>, Long> memo = new HashMap<>();

    public static void main(String[] args) {
        var total = 0L;
        for (var code : Util.readFileToLines()) {
            var paths = findNumKeypadPaths(code);
            var length = paths.stream()
                .mapToLong(path -> computeLength(path, 25))
                .min()
                .orElseThrow();
            total += length * Long.parseLong(code.substring(0, 3));
        }
        System.err.println(total);
    }

    private static List<String> findNumKeypadPaths(String code) {
        return Util.generateCombinations(getMoves(code).stream().map(NUM_PATHS::get).toList())
            .stream()
            .map(path -> String.join("", path))
            .toList();
    }

    private static long computeLength(String path, int depth) {
        var key = Pair.of(path, depth);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        if (depth == 1) {
            var length = getMoves(path).stream().mapToLong(DIR_LENGTHS::get).sum();
            memo.put(key, length);
            return length;
        }

        var length = 0L;
        for (var move : getMoves(path)) {
            length += DIR_PATHS.get(move).stream().mapToLong(subPath -> computeLength(subPath, depth - 1)).min().orElseThrow();
        }

        memo.put(key, length);
        return length;
    }

    private static List<String> getMoves(String path) {
        var actualPath = "A" + path;
        return IntStream.range(0, actualPath.length() - 1)
            .mapToObj(i -> actualPath.substring(i, i + 2))
            .toList();
    }

    private static Map<String, List<String>> computePaths(BiMap<Character, Point> keypad) {
        Map<String, List<String>> paths = new HashMap<>();

        for (var from : keypad.keySet()) {
            for (var to : keypad.keySet()) {
                var key = String.valueOf(from) + to;
                if (from == to) {
                    paths.put(key, List.of("A"));
                    continue;
                }

                List<String> possibilities = new ArrayList<>();
                var q = new ArrayDeque<>(List.of(new Node(keypad.get(from), "")));
                var optimal = Long.MAX_VALUE;
                while (!q.isEmpty()) {
                    var current = q.poll();
                    if (keypad.inverse().get(current.pos) == to) {
                        if (current.moves.length() < optimal) {
                            optimal = current.moves.length();
                        }
                        possibilities.add(current.moves + "A");
                    }
                    for (var dir : DIRMOVEMAP.entrySet()) {
                        var npos = new Point(current.pos, dir.getValue());
                        var nm = current.moves + dir.getKey();
                        var ch = keypad.inverse().get(npos);
                        if (ch == null || ch == ' ' || nm.length() > optimal)
                            continue;
                        q.offer(new Node(npos, nm));
                    }
                }
                paths.put(key, possibilities);
            }
        }
        return paths;
    }

}