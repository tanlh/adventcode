package _2024;

import util.IntPoint;
import util.Util;

import java.util.*;

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
        var obCount = 0;

        for (var obstruction : path) {
            if (grid[obstruction.y][obstruction.x] == '#' ||
                (obstruction.x == start.x && obstruction.y == start.y)) continue;

            grid[obstruction.y][obstruction.x] = '#';

            var newPath = tracePath();
            if (newPath.isEmpty()) {
                obCount++;
            }

            grid[obstruction.y][obstruction.x] = '.';
        }

        return obCount;
    }

    private static IntPoint findStart() {
        for (var i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '^') {
                    return new IntPoint(j, i);
                }
            }
        }
        throw new NoSuchElementException();
    }

}
