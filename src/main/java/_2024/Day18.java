package _2024;

import util.Point;
import util.Util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static util.Constants.DIRECTIONS;

public class Day18 {

    record Node(int x, int y, int score, int dir, Node previous) {}

    private static final int SIZE = 71;
//    private static final int SIZE = 7;

    public static void main(String[] args) {
        var bytes = Util.readFileToLines().stream()
            .map(Point::new)
            .toList();

        var part1 = traversal(bytes.subList(0, 1024));
        System.err.println("Part 1: " + part1);

        var part2 = IntStream.range(1024, bytes.size())
            .filter(i -> {
                System.err.println("Checking i: " + i);
                return traversal(bytes.subList(0, i)) == -1;
            })
            .findFirst().orElseThrow();
        System.err.println("Part 2: " + bytes.get(part2 - 1)); // because toIndex of sublist is exclusive
    }

    public static int traversal(List<Point> bytes) {
        var queue = new LinkedList<Node>();
        queue.offer(new Node(0, 0, 0, 0, null));

        var scores = new int[SIZE][SIZE][4];
        Arrays.stream(scores).flatMap(Arrays::stream).forEach(row -> Arrays.fill(row, Integer.MAX_VALUE));

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.x == SIZE - 1 && current.y == SIZE - 1) {
                return current.score;
            }

            for (int i = 0; i < 4; i++) {
                var x = current.x + DIRECTIONS[i][0];
                var y = current.y + DIRECTIONS[i][1];
                var score = current.score + 1;

                if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && !bytes.contains(new Point(x, y)) && score < scores[y][x][i]) {
                    scores[y][x][i] = score;
                    queue.offer(new Node(x, y, score, i, current));
                }
            }
        }

        return -1;
    }

}
