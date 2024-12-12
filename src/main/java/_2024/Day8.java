package _2024;

import util.Point;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 {

    private static char[][] grid;

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        var antennas = findAntennas();
        var antinodes = findAntinodes(antennas);
        System.err.println("Part 1: " + antinodes.size());

        // Part 2 includes antennas as antinodes
        antennas.values().stream()
            .flatMap(List::stream)
            .forEach(antenna -> antinodes.add(antenna.x + "," + antenna.y));
        System.err.println("Part 2: " + antinodes.size());
    }

    private static Map<Character, List<Point>> findAntennas() {
        return IntStream.range(0, grid.length).boxed()
            .flatMap(y -> IntStream.range(0, grid[y].length)
                .filter(x -> grid[y][x] != '.')
                .mapToObj(x -> Map.entry(grid[y][x], new Point(x, y))))
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toList())
            ));
    }

    private static Set<String> findAntinodes(Map<Character, List<Point>> antennasByFrequency) {
        return antennasByFrequency.values().stream()
            .flatMap(antennas ->
                IntStream.range(0, antennas.size()).boxed()
                    .flatMap(i -> IntStream.range(i + 1, antennas.size())
                        .mapToObj(j -> new Point[]{antennas.get(i), antennas.get(j)}))
            )
            .flatMap(pair -> findAntinodes(pair[0], pair[1]).stream())
            .collect(Collectors.toSet());
    }

    private static List<String> findAntinodes(Point a1, Point a2) {
        int minX = Math.min(a1.x, a2.x), maxX = Math.max(a1.x, a2.x), minY = Math.min(a1.y, a2.y), maxY = Math.max(a1.y, a2.y);
        int dx = a2.x - a1.x, dy = a2.y - a1.y;

        List<String> antinodes = new ArrayList<>();
        if (dx * dy >= 0) { // Direction: \
            addAntinodes(antinodes, minX, minY, -Math.abs(dx), -Math.abs(dy));
            addAntinodes(antinodes, maxX, maxY, Math.abs(dx), Math.abs(dy));
        } else { // Direction: /
            addAntinodes(antinodes, maxX, minY, Math.abs(dx), -Math.abs(dy));
            addAntinodes(antinodes, minX, maxY, -Math.abs(dx), Math.abs(dy));
        }

        return antinodes;
    }

    private static void addAntinodes(List<String> antinodes, int x, int y, int dx, int dy) {
        while (Util.isInGrid(grid, y + dy, x + dx)) { // Comment while to run part 1
            antinodes.add((x + dx) + "," + (y + dy));
            x += dx;
            y += dy;
        }
    }

}
