package _2023;

import util.Util;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Day17 {

    static final int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    static final char[] directionChars = {'<', '>', '^', 'v'};
    static final int minStep = 4;
    static final int maxStep = 10;

    public static void main(String[] args) {
        var lines = Util.readFileToLines();
        var grid = new int[lines.size()][lines.get(0).length()];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = Integer.parseInt(String.valueOf(lines.get(i).charAt(j)));
            }
        }

        System.out.println(findMinimumHeatLoss(grid));
    }

    private static void printPath(Node endNode, int rows, int cols) {
        char[][] path = new char[rows][cols];
        for (char[] row : path) {
            Arrays.fill(row, '.');
        }

        Node current = endNode;
        while (current != null) {
            path[current.x][current.y] = current.lastDirection;
            current = current.previous;
        }
        path[0][0] = 'S';
        path[rows - 1][cols - 1] = 'G';

        for (char[] row : path) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private static int findMinimumHeatLoss(int[][] map) {
        var rows = map.length;
        var cols = map[0].length;
        var bestHeatCost = new int[rows][cols][4][maxStep];
        for (int[][][] grid : bestHeatCost) {
            for (int[][] layer : grid) {
                for (int[] row : layer) {
                    Arrays.fill(row, Integer.MAX_VALUE);
                }
            }
        }

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, 0, 0, 0, ' ', null));
        Node endNode = null;

        while (!queue.isEmpty()) {
            var current = queue.poll();

            // Reach destination
            if (current.x == rows - 1 && current.y == cols - 1) {
                if (endNode == null || current.heatCost < endNode.heatCost) {
                    endNode = current;
                }
                continue;
            }

            var directionIndex = Arrays.binarySearch(directionChars, current.lastDirection);
            for (int i = 0; i < directions.length; i++) {
                if ((directionIndex == 0 && i == 1) || (directionIndex == 1 && i == 0)
                    || (directionIndex == 2 && i == 3) || (directionIndex == 3 && i == 2)) continue; // cannot backward

                var newX = current.x;
                var newY = current.y;
                var newHeatCost = current.heatCost;
                var outOfBounds = false;
                var changeDirection = i != directionIndex;
                var steps = changeDirection ? 0 : current.steps;

                for (int j = 0; j < (changeDirection ? minStep : 1); j++) {
                    newX += directions[i][0];
                    newY += directions[i][1];
                    steps++;

                    if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) {
                        outOfBounds = true;
                        break;
                    }

                    newHeatCost += map[newX][newY];
                }

                if (!outOfBounds && steps <= maxStep && newHeatCost < bestHeatCost[newX][newY][i][steps - 1]) {
                    bestHeatCost[newX][newY][i][steps - 1] = newHeatCost;
                    queue.add(new Node(newX, newY, newHeatCost, steps, directionChars[i], current));
                }
            }
        }

        if (endNode != null) {
            printPath(endNode, rows, cols);
            return endNode.heatCost;
        }

        return -1;
    }

    @AllArgsConstructor
    static class Node implements Comparable<Node> {
        int x, y;
        int heatCost;
        int steps;
        char lastDirection;
        Node previous;

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.heatCost, other.heatCost);
        }
    }
}
