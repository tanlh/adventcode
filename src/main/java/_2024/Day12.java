package _2024;

import util.Util;

import java.util.*;
import java.util.stream.IntStream;

import static util.Constants.DIRCHARMAP;

public class Day12 {

    private static char[][] grid;
    private static boolean[][] visited;

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        visited = new boolean[grid.length][grid[0].length];

        var results = IntStream.range(0, grid.length * grid[0].length)
            .filter(i -> !visited[i / grid[0].length][i % grid[0].length])
            .mapToObj(Day12::findRegion)
            .reduce(new int[2], (a, b) -> { a[0] += b[0]; a[1] += b[1]; return a; });
        System.out.printf("Part 1: %d%nPart 2: %d%n", results[0], results[1]);
    }

    private static int[] findRegion(int start) {
        int x = start % grid[0].length, y = start / grid[0].length;
        var plantType = grid[y][x];
        Queue<int[]> queue = new LinkedList<>(List.of(new int[]{x, y}));
        Map<Character, Map<Integer, Set<Integer>>> sideMap = new HashMap<>();
        visited[y][x] = true;
        int area = 0, perimeter = 0;

        while (!queue.isEmpty()) {
            var current = queue.poll();
            area++;
            for (var dir : DIRCHARMAP.entrySet()) {
                int newX = current[0] + dir.getValue()[0], newY = current[1] + dir.getValue()[1];
                if (!Util.isInGrid(grid, newY, newX) || grid[newY][newX] != plantType) {
                    perimeter++;
                    var side = dir.getKey();
                    var isHorizontal = side == 'T' || side == 'B';
                    sideMap.computeIfAbsent(side, _ -> new HashMap<>())
                        .computeIfAbsent(isHorizontal ? current[1] : current[0], _ -> new TreeSet<>())
                        .add(isHorizontal ? current[0] : current[1]);
                } else if (!visited[newY][newX]) {
                    queue.offer(new int[]{newX, newY});
                    visited[newY][newX] = true;
                }
            }
        }

        var sides = sideMap.values().stream()
            .flatMap(map -> map.values().stream().map(ArrayList::new))
            .mapToInt(cd -> IntStream.range(1, cd.size()).map(i -> cd.get(i) > cd.get(i-1) + 1 ? 1 : 0).sum() + 1)
            .sum();
        return new int[] { area * perimeter, area * sides };
    }

}
