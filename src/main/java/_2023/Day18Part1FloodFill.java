package _2023;

import util.LongPoint;
import util.Util;

import java.util.*;

import static util.Constants.DIRECTIONS;

public class Day18Part1FloodFill {

    static int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
    static int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

    public static void main(String[] args) {
        var points = buildBoundary(Util.readFileToLines());
        var result = calculateArea(points);
        System.out.println("Result: " + result);
    }

    private static List<LongPoint> buildBoundary(List<String> digPlan) {
        List<LongPoint> points = new ArrayList<>();
        int x = 0, y = 0;

        for (var step : digPlan) {
            var splitStep = step.split(" ");
            var direction = splitStep[0].charAt(0);
            var distance = Integer.parseInt(splitStep[1]);

            for (int i = 0; i < distance; i++) {
                x += direction == 'L' ? -1 : direction == 'R' ? 1 : 0;
                y += direction == 'U' ? -1 : direction == 'D' ? 1 : 0;
                points.add(new LongPoint(x, y));

                minX = Math.min(x, minX);
                maxX = Math.max(x, maxX);
                minY = Math.min(y, minY);
                maxY = Math.max(y, maxY);
            }
        }

        // Adjust min, max go outside the boundary
        minX--;
        minY--;
        maxX++;
        maxY++;

        return points;
    }

    private static long calculateArea(List<LongPoint> points) {
        var startPoint = new LongPoint(minX, minY);
        Set<LongPoint> outsidePoints = new HashSet<>();
        Queue<LongPoint> queue = new LinkedList<>();
        queue.add(startPoint);
        outsidePoints.add(startPoint);

        while (!queue.isEmpty()) {
            var point = queue.poll();

            for (var direction : DIRECTIONS) {
                var newX = point.x + direction[0];
                var newY = point.y + direction[1];
                var newPoint = new LongPoint(newX, newY);

                if (newX >= minX && newX <= maxX && newY >= minY && newY <= maxY
                    && !points.contains(newPoint) && outsidePoints.add(newPoint)) {
                    queue.add(newPoint);
                }
            }
        }

        var boundaryArea = (Math.abs(minX - maxX) + 1) * (Math.abs(minY - maxY) + 1);
        return boundaryArea - outsidePoints.size();
    }

}
