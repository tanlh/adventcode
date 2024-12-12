package _2022;

import util.LongPoint;
import util.Util;

import java.util.HashSet;
import java.util.Set;

public class Day14 {

    static long minX = Long.MAX_VALUE, maxX = Long.MIN_VALUE, maxY = Long.MIN_VALUE;
    static LongPoint source = new LongPoint(500, 0);

    public static void main(String[] args) {
        var border = buildBorder();
        System.err.println("Part 1: " + simulateSand(border, false));
        System.err.println("Part 2: " + simulateSand(border, true));
    }

    private static int simulateSand(Set<LongPoint> border, boolean hasFloor) {
        var settledSand = new HashSet<>(border);
        var count = 0;

        while (!settledSand.contains(source)) {
            var sand = new LongPoint(source.x, source.y);
            while (true) {
                if (hasFloor && sand.y == maxY + 1) break;
                if (!hasFloor && (sand.x < minX || sand.x > maxX || sand.y > maxY)) return count;

                if (!settledSand.contains(new LongPoint(sand.x, sand.y + 1))) {
                    // ignored
                } else if (!settledSand.contains(new LongPoint(sand.x - 1, sand.y + 1))) {
                    sand.x--;
                } else if (!settledSand.contains(new LongPoint(sand.x + 1, sand.y + 1))) {
                    sand.x++;
                } else {
                    break;
                }

                sand.y++;
            }

            settledSand.add(sand);
            count++;
        }
        return count;
    }

    private static Set<LongPoint> buildBorder() {
        Set<LongPoint> borders = new HashSet<>();

        for (var line : Util.readFileToLines()) {
            var points = line.split(" -> ");
            for (var i = 0; i < points.length - 1; i++) {
                var p1 = new LongPoint(Util.parseLine(points[i], ",", Long::parseLong));
                var p2 = new LongPoint(Util.parseLine(points[i + 1], ",", Long::parseLong));
                minX = Math.min(minX, Math.min(p1.x, p2.x));
                maxX = Math.max(maxX, Math.max(p1.x, p2.x));
                maxY = Math.max(maxY, Math.max(p1.y, p2.y));

                for (var x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
                    for (var y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); y++) {
                        borders.add(new LongPoint(x, y));
                    }
                }
            }
        }

        return borders;
    }
}