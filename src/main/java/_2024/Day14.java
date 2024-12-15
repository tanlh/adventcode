package _2024;

import util.Point;
import util.Util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.Constants.FULL_DIRECTIONS;

public class Day14 {

    record Robot(int x, int y, int vx, int vy) {
        Point move(int seconds) {
            return new Point(((x + seconds * vx) % WIDTH + WIDTH) % WIDTH, ((y + seconds * vy) % HEIGHT + HEIGHT) % HEIGHT);
        }
    }

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int CLUSTER = 40;

    public static void main(String[] args) {
        var robots = Util.readFileToLines().stream().map(line -> line.split(" "))
            .map(parts -> {
                var position = parts[0].substring(2).split(",");
                var velocity = parts[1].substring(2).split(",");
                return new Robot(Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(velocity[0]), Integer.parseInt(velocity[1]));
            })
            .toList();

        var part1 = solvePart1(robots);
        System.err.println("Part 1: " + part1);

        var part2 = IntStream.iterate(4000, i -> i + 1)
            .filter(i -> hasCluster(robots, i))
            .findFirst().orElseThrow();
        System.err.println("Part 2: " + part2);
        Util.printGrid(robots.stream().map(robot -> robot.move(part2)).toList(), HEIGHT, WIDTH);
    }

    private static long solvePart1(List<Robot> robots) {
        return robots.stream().map(robot -> robot.move(100))
            .map(p -> (p.x == 50 || p.y == 51) ? -1 : (p.x >= 50 ? 1 : 0) + (p.y >= 51 ? 2 : 0))
            .filter(q -> q != -1)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values().stream()
            .reduce(1L, (a, b) -> a * b);
    }

    private static boolean hasCluster(List<Robot> robots, int seconds) {
        var points = robots.stream().map(robot -> robot.move(seconds)).collect(Collectors.toSet());
        Set<Point> visited = new HashSet<>();

        return points.stream().anyMatch(start -> {
            if (visited.contains(start)) return false;

            var stack = new ArrayDeque<Point>();
            stack.push(start);
            var clusterSize = 0;

            while (!stack.isEmpty()) {
                var current = stack.pop();
                if (visited.add(current) && ++clusterSize >= CLUSTER) return true;

                Arrays.stream(FULL_DIRECTIONS)
                    .map(dir -> new Point((current.x + dir[0] + WIDTH) % WIDTH, (current.y + dir[1] + HEIGHT) % HEIGHT))
                    .filter(p -> points.contains(p) && !visited.contains(p))
                    .forEach(stack::push);
            }
            return false;
        });
    }
}
