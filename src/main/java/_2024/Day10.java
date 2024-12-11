package _2024;

import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

import static util.Constants.DIRECTIONS;

public class Day10 {

    private static int[][] grid;

    public static void main(String[] args) {
        grid = Util.readFileToGridInt();
        var result = IntStream.range(0, grid.length).boxed()
            .flatMap(i -> IntStream.range(0, grid[0].length)
                .filter(j -> grid[i][j] == 0)
                .mapToObj(j -> countTrailsAndPaths(j, i)))
            .reduce(Pair.of(0, 0),
                (a, b) -> Pair.of(a.getLeft() + b.getLeft(), a.getRight() + b.getRight()));
        System.err.println("Result: " + result);
    }

    private static Pair<Integer, Integer> countTrailsAndPaths(int startX, int startY) {
        record State(int x, int y, int value) {}

        var nines = new HashSet<Pair<Integer, Integer>>();
        var queue = new LinkedList<State>();
        var pathCounts = new int[grid.length][grid[0].length];
        var visited = new boolean[grid.length][grid[0].length];

        queue.offer(new State(startX, startY, 0));
        pathCounts[startY][startX] = 1;
        var totalPaths = 0;

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.value == 9) {
                nines.add(Pair.of(current.x, current.y));
                totalPaths += pathCounts[current.y][current.x];
                continue;
            }

            for (var direction : DIRECTIONS) {
                int newX = current.x + direction[0], newY = current.y + direction[1];
                if (Util.isInGrid(grid, newY, newX) && grid[newY][newX] == current.value + 1) {
                    pathCounts[newY][newX] += pathCounts[current.y][current.x];
                    if (!visited[newY][newX]) {
                        queue.offer(new State(newX, newY, current.value + 1));
                        visited[newY][newX] = true;
                    }
                }
            }
        }

        return Pair.of(nines.size(), totalPaths);
    }

}
