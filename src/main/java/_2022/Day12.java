package _2022;

import util.Node;
import util.Util;

import java.util.Comparator;
import java.util.PriorityQueue;

import static util.Constants.DIRECTIONS;
import static util.Constants.DIRMAP;

public class Day12 {

    static char[][] grid;
    static int rows, cols, endX, endY;

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        rows = grid.length;
        cols = grid[0].length;

        int startX = -1, startY = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'S') {
                    grid[i][j] = 'a';
                    startX = j;
                    startY = i;
                }
                if (grid[i][j] == 'E') {
                    grid[i][j] = 'z';
                    endX = j;
                    endY = i;
                }
            }
        }
        System.err.println("Part 1: " + findShortestPath(startX, startY));


        var shortest = Integer.MAX_VALUE;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'a') {
                    shortest = Math.min(shortest, findShortestPath(j, i));
                }
            }
        }
        System.err.println("Part 2: " + shortest);
    }

    private static int findShortestPath(int startX, int startY) {
        var visited = new boolean[rows][cols];

        var startNode = new Node(startX, startY);
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(Node::getSteps));
        queue.add(startNode);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (visited[current.y][current.x]) {
                continue;
            }

            visited[current.y][current.x] = true;

            if (current.x == endX && current.y == endY) {
//                Util.printPath(startNode, current, rows, cols);
                return current.steps;
            }

            for (int i = 0; i < 4; i++) {
                var newX = current.x + DIRECTIONS[i][0];
                var newY = current.y + DIRECTIONS[i][1];

                if (newX < 0 || newX >= cols || newY < 0 || newY >= rows || visited[newY][newX]) {
                    continue;
                }

                var curElevation = grid[current.y][current.x];
                var nextElevation = grid[newY][newX];

                if (nextElevation > (curElevation + 1)) {
                    continue;
                }

                queue.add(new Node(newX, newY, current.steps + 1, 0, DIRMAP.inverse().get(i), current));
            }
        }

        return Integer.MAX_VALUE;
    }

}