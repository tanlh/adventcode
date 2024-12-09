package _2024;

import util.IntPoint;
import util.Util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static util.Constants.TOP;

public class Day6 {

    private static char[][] grid;
    private static IntPoint start;

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        start = findStart();

        var path = tracePath();
        System.err.println("Part 1: " + path.size());

        var obstructions = countObstruction(path);
        System.err.println("Part 2: " + obstructions);
    }

    private static Set<IntPoint> tracePath() {
        Set<IntPoint> path = new LinkedHashSet<>();
        Set<String> visited = new HashSet<>();
        var current = new IntPoint(start.x, start.y);
        var direction = TOP;

        while (Util.isInGrid(grid, current.y, current.x)) {
            var key = current.x + "," + current.y + ":" + direction[0] + "," + direction[1];
            if (!visited.add(key)) {
                // Return empty set if found loop
                return Set.of();
            }

            path.add(new IntPoint(current.x, current.y));

            var newX = current.x + direction[0];
            var newY = current.y + direction[1];
            if (!Util.isInGrid(grid, newY, newX)) break;

            if (grid[newY][newX] == '#') {
                direction = new int[]{-direction[1], direction[0]};
            } else {
                current.x = newX;
                current.y = newY;
            }
        }

        return path;
    }

    private static int countObstruction(Set<IntPoint> path) {
        return path.stream()
            .filter(obs -> grid[obs.y][obs.x] != '#' && !obs.equals(start))
            .mapToInt(obs -> {
                grid[obs.y][obs.x] = '#';
                var newPath = tracePath();
                grid[obs.y][obs.x] = '.';
                return newPath.isEmpty() ? 1 : 0;
            })
            .sum();
    }

    private static IntPoint findStart() {
        return IntStream.range(0, grid.length)
            .mapToObj(y -> IntStream.range(0, grid[0].length)
                .filter(x -> grid[y][x] == '^')
                .mapToObj(j -> new IntPoint(j, y)))
            .flatMap(Function.identity())
            .findFirst()
            .orElseThrow();
    }

}
