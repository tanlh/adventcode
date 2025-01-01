package _2024;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import util.Point;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.Constants.DIRMOVEMAP;

public class Day21 {

    private static final Map<String, List<String>> NUM_PATHS = preComputePaths(createKeypad("789", "456", "123", " 0A"));
    private static final Map<String, List<String>> DIR_PATHS = preComputePaths(createKeypad(" ^A", "<v>"));
    private static final Map<String, Integer> DIR_LENGTHS = Maps.transformEntries(DIR_PATHS, (_, v) -> v.getFirst().length());
    private static final Map<Pair<String, Integer>, Long> memo = new HashMap<>();

    public static void main(String[] args) {
        var result = Util.readFileToLines().stream()
            .mapToLong(code -> Util.generateCombinations(getMoves(code).stream().map(NUM_PATHS::get).toList())
                .stream()
                .mapToLong(path -> computeLength(String.join("", path), 25))
                .min().orElseThrow() * Long.parseLong(code.substring(0, 3)))
            .sum();
        System.err.println("Result: " + result);
    }

    private static long computeLength(String path, int depth) {
        var key = Pair.of(path, depth);
        if (memo.containsKey(key)) return memo.get(key);

        var length = depth == 1
            ? getMoves(path).stream().mapToLong(DIR_LENGTHS::get).sum()
            : getMoves(path).stream().mapToLong(move -> DIR_PATHS.get(move).stream()
            .mapToLong(subPath -> computeLength(subPath, depth - 1))
            .min().orElseThrow()).sum();
        memo.put(key, length);
        return length;
    }

    private static List<String> getMoves(String path) {
        return IntStream.range(0, path.length())
            .mapToObj(i -> (i == 0 ? "A" : String.valueOf(path.charAt(i - 1))) + path.charAt(i))
            .toList();
    }

    private static BiMap<Character, Point> createKeypad(String... rows) {
        return IntStream.range(0, rows.length).boxed()
            .flatMap(y -> IntStream.range(0, rows[y].length())
                .filter(x -> rows[y].charAt(x) != ' ')
                .mapToObj(x -> Pair.of(rows[y].charAt(x), new Point(x, y))))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (_, b) -> b, HashBiMap::create));
    }

    private static Map<String, List<String>> preComputePaths(BiMap<Character, Point> keypad) {
        return keypad.keySet().stream()
            .flatMap(from -> keypad.keySet().stream()
                .map(to -> Pair.of(from.toString() + to, from == to ? List.of("A") : findShortestPath(keypad, from, to))))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static List<String> findShortestPath(BiMap<Character, Point> keypad, char from, char to) {
        record Node(Point pos, String moves) {}
        var queue = new ArrayDeque<>(List.of(new Node(keypad.get(from), "")));
        var shortest = new int[]{Integer.MAX_VALUE};
        List<String> shortestPaths = new ArrayList<>();

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (keypad.inverse().get(current.pos) == to) {
                if (current.moves.length() < shortest[0]) {
                    shortest[0] = current.moves.length();
                    shortestPaths.clear();
                }
                shortestPaths.add(current.moves + "A");
                continue;
            }

            DIRMOVEMAP.forEach((move, dir) -> {
                var nextPos = new Point(current.pos, dir);
                var nextMoves = current.moves + move;
                var nextButton = keypad.inverse().get(nextPos);
                if (nextButton == null || nextButton == ' ' || nextMoves.length() > shortest[0]) return;
                queue.offer(new Node(nextPos, nextMoves));
            });
        }

        return shortestPaths;
    }

}