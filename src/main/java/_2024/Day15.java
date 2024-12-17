package _2024;

import util.Point;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.Constants.DIRMOVEMAP;

public class Day15 {

    enum Tile {EMPTY, WALL, BOX_LEFT, BOX_RIGHT, ROBOT}

    private static final Map<Point, Tile> grid = new HashMap<>();
    private static Point robot;
    private static int height, width;

    public static void main(String[] args) {
        var input = Util.readFileToBlocks();
        var moves = String.join("", input.get(1));

        // Part 1
        parseGrid(input.getFirst(), 1);
        moves.chars().forEach(Day15::moveRobot1);
        System.err.println("Part 1: " + calculateGPSSum());

        // Part 2
        parseGrid(input.getFirst(), 2);
        moves.chars().forEach(Day15::moveRobot2);
        System.err.println("Part 2: " + calculateGPSSum());
    }

    private static void parseGrid(List<String> gridLines, int part) {
        grid.clear();
        height = gridLines.size();
        width = gridLines.getFirst().length() * part;
        IntStream.range(0, height).boxed().flatMap(y -> IntStream.range(0, width / part).boxed()
            .flatMap(x -> IntStream.range(0, part).mapToObj(i -> {
                var p = new Point(x * part + i, y);
                return Map.entry(p, switch (gridLines.get(y).charAt(x)) {
                    case '#' -> Tile.WALL;
                    case 'O' -> i == 0 ? Tile.BOX_LEFT : Tile.BOX_RIGHT;
                    case '@' -> {
                        if (i == 0) robot = p;
                        yield i == 0 ? Tile.ROBOT : Tile.EMPTY;
                    }
                    default -> Tile.EMPTY;
                });
            }))).forEach(entry -> grid.put(entry.getKey(), entry.getValue()));
    }

    private static void moveRobot1(int move) {
        var dir = DIRMOVEMAP.get((char) move);
        var boxes = new ArrayList<Point>();
        var current = robot;

        do {
            if (grid.get(current = new Point(current, dir)) == Tile.WALL) return;
            if (grid.get(current) == Tile.BOX_LEFT) boxes.add(current);
        } while (grid.get(current) == Tile.BOX_LEFT);

        boxes.reversed().forEach(old -> {
            grid.put(new Point(old, dir), Tile.BOX_LEFT);
            grid.put(old, Tile.EMPTY);
        });

        grid.put(robot, Tile.EMPTY);
        grid.put(robot = new Point(robot, dir), Tile.ROBOT);
    }

    private static void moveRobot2(int move) {
        var dir = DIRMOVEMAP.get((char) move);
        var next = new Point(robot, dir);
        if (grid.get(next) == Tile.WALL) return;

        var boxes = new HashSet<Point>();
        if (dir[0] != 0) {
            for (var current = next; ; current = new Point(current, dir)) {
                var tile = grid.get(current);
                if (tile == Tile.EMPTY) break;
                if (tile == Tile.WALL) return;
                boxes.add(current);
                boxes.add(new Point(current.x + (tile == Tile.BOX_LEFT ? 1 : -1), current.y));
            }
        } else {
            var toCheck = new LinkedList<>(List.of(next));
            while (!toCheck.isEmpty()) {
                var current = toCheck.poll();
                var tile = grid.get(current);
                if (tile == Tile.WALL) return;
                if ((tile == Tile.BOX_LEFT || tile == Tile.BOX_RIGHT) && boxes.add(current)) {
                    var other = new Point(current.x + (tile == Tile.BOX_LEFT ? 1 : -1), current.y);
                    boxes.add(other);
                    toCheck.addAll(List.of(new Point(current, dir), new Point(other, dir)));
                }
            }
        }

        boxes.stream()
            .sorted(Comparator.comparingInt((Point p) -> (p.x + p.y) * (dir[0] + dir[1] > 0 ? -1 : 1)))
            .forEach(box -> {
                grid.put(new Point(box, dir), grid.get(box));
                grid.put(box, Tile.EMPTY);
            });
        grid.put(robot, Tile.EMPTY);
        grid.put(robot = next, Tile.ROBOT);
        printGrid();
    }

    private static void printGrid() {
        IntStream.range(0, height).forEach(y -> System.out.println(IntStream.range(0, width)
            .mapToObj(x -> switch (grid.get(new Point(x, y))) {
                case WALL -> "#";
                case BOX_LEFT -> "[";
                case BOX_RIGHT -> "]";
                case ROBOT -> "@";
                default -> ".";
            })
            .collect(Collectors.joining())));
        System.out.println();
    }

    private static int calculateGPSSum() {
        return grid.entrySet().stream()
            .filter(entry -> entry.getValue() == Tile.BOX_LEFT)
            .mapToInt(entry -> entry.getKey().y * 100 + entry.getKey().x)
            .sum();
    }

}
