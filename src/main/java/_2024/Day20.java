package _2024;

import util.Point;
import util.Util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static util.Constants.DIRECTIONS;

public class Day20 {

    record Node(int x, int y, int score) {}

    private static final int MAX_CHEAT_STEPS = 20, SAVE_TIME = 100;
    private static char[][] grid;
    private static int[][] distances;
    private static Point start, end;
    private static int normalPath;

    public static void main(String[] args) {
        parseInput();

        traversal(start, next -> {
            if (grid[next.y][next.x] == '#') return false;
            distances[next.y][next.x] = normalPath - next.score;
            return next.x != end.x || next.y != end.y;
        });

        var result = IntStream.range(0, grid.length).parallel()
            .mapToLong(y -> IntStream.range(0, grid[0].length)
                .filter(x -> grid[y][x] != '#')
                .mapToLong(x -> countCheat(x, y))
                .sum())
            .sum();
        System.err.println("Result: " + result);
    }

    private static void parseInput() {
        grid = Util.readFileToGrid();
        distances = new int[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != '#') normalPath++;
                if (grid[y][x] == 'S') start = new Point(x, y);
                if (grid[y][x] == 'E') end = new Point(x, y);
            }
        distances[start.y][start.x] = --normalPath;
    }

    private static long countCheat(int startX, int startY) {
        int[] cheats = {0};
        traversal(new Point(startX, startY), next -> {
            if (next.score <= MAX_CHEAT_STEPS && next.score >= 2 && grid[next.y][next.x] != '#' &&
                distances[startY][startX] - (next.score + distances[next.y][next.x]) >= SAVE_TIME) cheats[0]++;
            return next.score <= MAX_CHEAT_STEPS;
        });
        return cheats[0];
    }

    private static void traversal(Point start, Predicate<Node> process) {
        var queue = new ArrayDeque<Node>();
        var visited = new boolean[grid.length][grid[0].length];
        queue.offer(new Node(start.x, start.y, 0));
        visited[start.y][start.x] = true;

        while (!queue.isEmpty()) {
            var current = queue.poll();
            Arrays.stream(DIRECTIONS).forEach(dir -> {
                int x = current.x + dir[0], y = current.y + dir[1];
                Node next;
                if (Util.isInGrid(grid, y, x) && !visited[y][x] && process.test(next = new Node(x, y, current.score + 1))) {
                    visited[y][x] = true;
                    queue.offer(next);
                }
            });
        }
    }

}
