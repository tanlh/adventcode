package _2023;

import util.Constants;
import util.Node;
import util.Util;

import java.util.Arrays;
import java.util.Comparator;
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

        Node start = new Node(0, 0), end = null;
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getWeight));
        queue.add(start);

        while (!queue.isEmpty()) {
            var current = queue.poll();

            if (current.x == cols - 1 && current.y == rows - 1) {
                if (end == null || current.weight < end.weight) {
                    end = current;
                }
                continue;
            }

            var directionIndex = DIRMAP.getOrDefault(current.direction, -1);
            for (int i = 0; i < Constants.DIRECTIONS.length; i++) {
                // cannot backward
                if ((directionIndex == 0 && i == 1) || (directionIndex == 1 && i == 0)
                    || (directionIndex == 2 && i == 3) || (directionIndex == 3 && i == 2)) continue;

                var newX = current.x;
                var newY = current.y;
                var newHeatCost = current.weight;
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

        Util.printPath(start, end, rows, cols);

        System.out.println(end.weight);
    }

}
