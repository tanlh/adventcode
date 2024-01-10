package _2023;

import util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class Day21 {

    static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    static long a, b, c;

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();

        // because of parity, odd steps is kept, even steps plus 1
        var point = countPoints(grid, 64);
        System.err.println("Part 1: " + (point + 1));

        var steps = 65;
        var point0 = countPoints(expandGrid(grid, 1), steps);
        System.err.println("k=0: " + point0);

        steps = 65 + 131 * 2;
        var point2 = countPoints(expandGrid(grid, 2), steps);
        System.err.println("k=2: " + point2);

        steps = 65 + 131 * 4;
        var point4 = countPoints(expandGrid(grid, 4), steps);
        System.err.println("k=4: " + point4);

        // k=0: 3753
        //k=1: 33614
        //k=2: 93253
        //k=3: 182666
        //k=4: 301857
        //k=5: 450822
        //k=6: 629565
        //k=7: 838082

        long d1 = point2 - point0;
        long d2 = (point4 - point0) / 2;
        a = (d2 - d1) / 4;
        b = d1 / 2 - 2 * a;
        c = point0;
        System.err.println("a, b, c: " + a + ", " + b + ", " + c);

        printResult(1L);
        printResult(2L);
        printResult(3L);
        printResult(4L);
        printResult(5L);
        printResult(6L);
        printResult(7L);
        printResult((26501365L - 65) / 131);
    }

    private static void printResult(long k) {
        long result = a * k * k + b * k + c;
        System.err.println("Result k=" + k + ": " + result);
    }

    public static char[][] expandGrid(char[][] original, int layers) {
        var rows = original.length;
        var cols = original[0].length;
        var totalRows = rows + (2 * layers * rows);
        var totalCols = cols + (2 * layers * cols);
        var expanded = new char[totalRows][totalCols];

        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCols; j++) {
                var originalI = (i + rows * layers) % rows;
                var originalJ = (j + cols * layers) % cols;
                var value = original[originalI][originalJ];
                if (value == 'S' && (i < rows * layers || i >= (rows * layers + rows) || j < cols * layers || j >= (cols * layers + cols))) {
                    value = '.';
                }

                expanded[i][j] = value;
            }
        }

        return expanded;
    }

    private static int countPoints(char[][] grid, int steps) {
        var rows = grid.length;
        var cols = grid[0].length;
        boolean[][][] visited = new boolean[rows][cols][steps + 1];

        var startX = 0;
        var startY = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                }
            }
        }

        Set<Point> reachablePoints = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        queue.offer(new Point(startX, startY, 0));
        visited[startX][startY][0] = true;

        while (!queue.isEmpty()) {
            var curr = queue.poll();
            var newStep = curr.steps + 1;

            for (int[] dir : DIRECTIONS) {
                var newX = curr.x + dir[0];
                var newY = curr.y + dir[1];

                if (curr.steps == steps) {
                    reachablePoints.add(curr);
                    continue;
                }

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols &&
                    grid[newX][newY] == '.' && !visited[newX][newY][newStep]) {
                    visited[newX][newY][newStep] = true;
                    if (newStep == steps) {
                        reachablePoints.add(new Point(newX, newY, steps));
                    } else {
                        queue.offer(new Point(newX, newY, newStep));
                    }
                }
            }
        }

        printPoints(grid, reachablePoints);

        return reachablePoints.size(); // add the S point
    }

    private static void printPoints(char[][] map, Set<Point> points) {
        char[][] copyMap = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            copyMap[i] = map[i].clone();
        }

        points.forEach(point -> copyMap[point.x][point.y] = '0');

        for (char[] row : copyMap) {
            for (char ch : row) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }

    @Data
    @AllArgsConstructor
    static class Point {
        int x;
        int y;
        int steps;
    }

}
