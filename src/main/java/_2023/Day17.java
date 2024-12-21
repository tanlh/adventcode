package _2023;

import util.Constants;
import util.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import static util.Constants.DIRECTIONS;
import static util.Constants.DIRMAP;

public class Day17 {

    record Node(int x, int y, int steps, int weight, char direction, Node previous) {}

    static final int minStep = 4;
    static final int maxStep = 10;

    public static void main(String[] args) {
        var map = Util.readFileToGridInt();
        var rows = map.length;
        var cols = map[0].length;
        var lowestWeight = new int[rows][cols][4][maxStep];
        for (int[][][] grid : lowestWeight) {
            for (int[][] layer : grid) {
                for (int[] row : layer) {
                    Arrays.fill(row, Integer.MAX_VALUE);
                }
            }
        }

        var queue = new PriorityQueue<>(Comparator.comparing(Node::weight));
        queue.add(new Node(0, 0, 0, 0, ' ', null));

        while (!queue.isEmpty()) {
            var current = queue.poll();

            if (current.x == cols - 1 && current.y == rows - 1) {
//                Util.printPath(start, end, rows, cols);
                System.err.println("Result: " + current.weight);
                break;
            }

            var dirIndex = DIRMAP.getOrDefault(current.direction, -1);
            for (int i = 0; i < DIRECTIONS.length; i++) {
                // cannot backward: 2 (U) * 3 (D) = 6, 0 (L) + 1 (R) = 1
                if (dirIndex * i == 6 || dirIndex + i == 1) continue;

                var x = current.x;
                var y = current.y;
                var weight = current.weight;
                var outOfBound = false;
                var changeDirection = i != dirIndex;
                var steps = changeDirection ? 0 : current.steps;

                for (int j = 0; j < (changeDirection ? minStep : 1); j++) {
                    x += Constants.DIRECTIONS[i][0];
                    y += Constants.DIRECTIONS[i][1];
                    steps++;

                    if (x < 0 || x >= cols || y < 0 || y >= rows) {
                        outOfBound = true;
                        break;
                    }

                    weight += map[x][y];
                }

                if (!outOfBound && steps <= maxStep && weight < lowestWeight[y][x][i][steps - 1]) {
                    lowestWeight[y][x][i][steps - 1] = weight;
                    queue.add(new Node(x, y, steps, weight, DIRMAP.inverse().get(i), current));
                }
            }
        }
    }

}
