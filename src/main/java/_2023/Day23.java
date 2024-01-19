package _2023;

import util.Util;

import java.util.*;

import static util.Constants.DIRECTIONS;
import static util.Constants.DIRMAP;

public class Day23 {

    static int maxSteps = -1;

    /**
     * part 2 run too slow but still return the result :))
     */
    public static void main(String[] args) {
        var grid = Util.readFileToGrid();

        Stack<int[]> stack = new Stack<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        // row, col, steps
        stack.push(new int[]{1, 1, 0});

        while (!stack.isEmpty()) {
            var state = stack.pop();
            var x = state[0];
            var y = state[1];
            var steps = state[2];

            if (steps == -1) {
                visited[y][x] = false;
                continue;
            }

            if (x < 0 || y < 0 || x >= grid[0].length || y >= grid.length || grid[y][x] == '#' || visited[y][x]) {
                continue;
            }

            if (x == grid[0].length - 2 && y == grid.length - 2) {
                maxSteps = Math.max(maxSteps, steps + 1);
                continue;
            }

            visited[y][x] = true;
            // Backtrack for current cell
            stack.push(new int[]{x, y, -1});

            // To run part 2, comment this if and keep the else
            var terrain = grid[y][x];
            if (DIRMAP.containsKey(terrain)) {
                var dirIndex = DIRMAP.get(terrain);
                stack.push(new int[]{x + DIRECTIONS[dirIndex][0], y + DIRECTIONS[dirIndex][1], steps + 1});
            } else {
                for (var dir : DIRECTIONS) {
                    stack.push(new int[]{x + dir[0], y + dir[1], steps + 1});
                }
            }
        }

        System.out.println("Result: " + maxSteps);
    }

}
