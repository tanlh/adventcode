package com.alibaba.logistics.station;

import java.util.*;

public class Day23 {

    static int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    static Map<Character, Integer> SLOPES_DIRECTION = Map.of(
        '^', 0,
        '>', 1,
        'v', 2,
        '<', 3
    );
    static int maxSteps = -1;

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();

        dfs(grid); // part 2 run too slow but still return the result :))
        System.out.println("Result: " + maxSteps);
    }

    private static void dfs(char[][] grid) {
        Stack<int[]> stack = new Stack<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        stack.push(new int[]{1, 1, 0}); // row, col, steps

        while (!stack.isEmpty()) {
            int[] state = stack.pop();
            int row = state[0];
            int col = state[1];
            int steps = state[2];

            if (steps == -1) {
                visited[row][col] = false;
                continue;
            }

            if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || grid[row][col] == '#' || visited[row][col]) {
                continue;
            }

            if (row == grid.length - 1 && col == grid[0].length - 2) {
                maxSteps = Math.max(maxSteps, steps + 1);
                continue;
            }

            visited[row][col] = true;
            stack.push(new int[]{row, col, -1}); // Backtrack for current cell

            //        var terrain = map[row][col]; // part 1
//        if (SLOPES_DIRECTION.containsKey(terrain)) {
//            var dirIndex = SLOPES_DIRECTION.get(terrain);
//            stack.push(new int[] {row + DIRECTIONS[dirIndex][0], col + DIRECTIONS[dirIndex][1], visited, steps + 1});
//        } else {
            for (var dir : DIRECTIONS) {
                stack.push(new int[]{row + dir[0], col + dir[1], steps + 1});
            }
//        }
        }
    }

}
