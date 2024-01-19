package _2023;

import util.Constants;
import util.Util;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.PriorityQueue;

import static util.Constants.DIRMAP;

public class Day17 {

    static final int minStep = 4;
    static final int maxStep = 10;

    public static void main(String[] args) {
        var map = Util.readFileToGridInt();
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
            if (current.x == cols - 1 && current.y == rows - 1) {
                if (endNode == null || current.heatCost < endNode.heatCost) {
                    endNode = current;
                }
                continue;
            }

            var directionIndex = DIRMAP.getOrDefault(current.lastDirection, -1);
            for (int i = 0; i < Constants.DIRECTIONS.length; i++) {
                // cannot backward
                if ((directionIndex == 0 && i == 1) || (directionIndex == 1 && i == 0)
                    || (directionIndex == 2 && i == 3) || (directionIndex == 3 && i == 2)) continue;

                var newX = current.x;
                var newY = current.y;
                var newHeatCost = current.heatCost;
                var outOfBounds = false;
                var changeDirection = i != directionIndex;
                var steps = changeDirection ? 0 : current.steps;

                for (int j = 0; j < (changeDirection ? minStep : 1); j++) {
                    newX += Constants.DIRECTIONS[i][0];
                    newY += Constants.DIRECTIONS[i][1];
                    steps++;

                    if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) {
                        outOfBounds = true;
                        break;
                    }

                    newHeatCost += map[newX][newY];
                }

                if (!outOfBounds && steps <= maxStep && newHeatCost < bestHeatCost[newY][newX][i][steps - 1]) {
                    bestHeatCost[newY][newX][i][steps - 1] = newHeatCost;
                    queue.add(new Node(newX, newY, newHeatCost, steps, DIRMAP.inverse().get(i), current));
                }
            }
        }

        printPath(endNode, rows, cols);
        System.out.println(endNode.heatCost);
    }

    private static void printPath(Node endNode, int rows, int cols) {
        char[][] path = new char[rows][cols];
        for (char[] row : path) {
            Arrays.fill(row, '.');
        }

        Node current = endNode;
        while (current != null) {
            path[current.y][current.x] = current.lastDirection;
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
