package _2024;

import util.Util;

import java.util.*;
import java.util.stream.Stream;

import static util.Constants.DIRECTIONS;

public class Day16 {

    record Node(int x, int y, int score, int dir, Node previous) {}

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();

        var queue = new PriorityQueue<>(Comparator.comparing(Node::score));
        queue.add(new Node(1, grid.length - 2, 0, 0, null));

        var scores = new int[grid.length][grid[0].length][4];
        Arrays.stream(scores).flatMap(Arrays::stream).forEach(row -> Arrays.fill(row, Integer.MAX_VALUE));

        var paths = new ArrayList<Node>();

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.x == grid[0].length - 2 && current.y == 1) {
                paths.add(current);
            }

            for (int i = 0; i < 4; i++) {
                var x = current.x + DIRECTIONS[i][0];
                var y = current.y + DIRECTIONS[i][1];
                var score = current.score + (i != current.dir ? 1001 : 1);

                if (Util.isInGrid(grid, y, x) && grid[y][x] != '#' && score <= scores[y][x][i]) {
                    scores[y][x][i] = score;
                    queue.add(new Node(x, y, score, i, current));
                }
            }
        }

        var minScore = paths.stream().map(Node::score).min(Integer::compareTo).orElse(0);
        System.err.println("Min score: " + minScore);

        var bestTiles = paths.stream()
            .filter(path -> path.score == minScore)
            .flatMap(path -> Stream.iterate(path, Objects::nonNull, Node::previous))
            .map(node -> node.x + "," + node.y)
            .distinct().count();
        System.err.println("Best tiles: " + bestTiles);
    }

}
