package _2022;

import lombok.AllArgsConstructor;
import util.Point;
import util.Util;

import java.util.*;

public class Day9 {

    static final int TAIL_NUM = 9;

    @AllArgsConstructor
    enum Direction {
        R(1, 0), L(-1, 0), U(0, 1), D(0, -1);

        final int dx, dy;
    }

    public static void main(String[] args) {
        Set<Point> visited = new HashSet<>();
        visited.add(new Point(0, 0));

        List<Point> rope = new ArrayList<>();
        for (int i = 0; i <= TAIL_NUM; i++) {
            rope.add(new Point(0, 0));
        }

        for (var move : Util.readFileToLines()) {
            var parts = move.split(" ");
            var dir = Direction.valueOf(parts[0]);

            for (int i = 0; i < Integer.parseInt(parts[1]); i++) {
                var head = rope.get(0);
                head.x += dir.dx;
                head.y += dir.dy;

                for (int j = 1; j < rope.size(); j++) {
                    var knot1 = rope.get(j - 1);
                    var knot2 = rope.get(j);

                    if (Math.abs(knot1.x - knot2.x) > 1 || Math.abs(knot1.y - knot2.y) > 1) {
                        knot2.x += Long.compare(knot1.x, knot2.x);
                        knot2.y += Long.compare(knot1.y, knot2.y);
                    }

                    if (j == TAIL_NUM) {
                        visited.add(new Point(knot2.x, knot2.y));
                    }
                }
            }
        }

        System.err.println("Visited: " + visited.size());
    }

}