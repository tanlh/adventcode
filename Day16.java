import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day16 {

    public static void main(String[] args) {
        var grid = readFileToGrid();
        System.out.println("Energized tiles: " + findMaxEnergizedConfiguration(grid));
    }

    private static int findMaxEnergizedConfiguration(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int maxEnergized = 0;

        for (int y = 0; y < cols; y++) {
            maxEnergized = Math.max(maxEnergized, countEnergizedTiles(grid, 0, y, 1, 0));
        }

        for (int y = 0; y < cols; y++) {
            maxEnergized = Math.max(maxEnergized, countEnergizedTiles(grid, rows - 1, y, -1, 0));
        }

        for (int x = 0; x < rows; x++) {
            maxEnergized = Math.max(maxEnergized, countEnergizedTiles(grid, x, 0, 0, 1));
        }

        for (int x = 0; x < rows; x++) {
            maxEnergized = Math.max(maxEnergized, countEnergizedTiles(grid, x, cols - 1, 0, -1));
        }

        return maxEnergized;
    }

    @SneakyThrows
    private static char[][] readFileToGrid() {
        List<String> lines = Files.readAllLines(Path.of("file.txt"));

        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    private static int countEnergizedTiles(char[][] grid, int startX, int startY, int startDx, int startDy) {
        int rows = grid.length;
        int cols = grid[0].length;
        Set<String> energizedTiles = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Queue<Beam> beams = new LinkedList<>();

        beams.add(new Beam(startX, startY, startDx, startDy));

        while (!beams.isEmpty()) {
            var beam = beams.remove();
            if (beam.x < 0 || beam.x >= rows || beam.y < 0 || beam.y >= cols) {
                continue;
            }

            var state = beam.x + "," + beam.y + "," + beam.dx + "," + beam.dy;
            if (visited.contains(state)) {
                continue;
            }

            visited.add(state);
            energizedTiles.add(beam.x + "," + beam.y);

            var tile = grid[beam.x][beam.y];
            if (tile == '/' || tile == '\\') {
                beam.reflect(tile);
                beam.move();
                beams.add(beam);
            } else if (tile == '|' && beam.dy != 0) {
                beams.add(new Beam(beam.x, beam.y, beam.dy, 0));
                beams.add(new Beam(beam.x, beam.y, -beam.dy, 0));
            } else if (tile == '-' && beam.dx != 0) {
                beams.add(new Beam(beam.x, beam.y, 0, beam.dx));
                beams.add(new Beam(beam.x, beam.y, 0, -beam.dx));
            } else {
                beam.move();
                beams.add(beam);
            }
        }

        return energizedTiles.size();
    }


    private static class Beam {
        int x, y, dx, dy;

        Beam(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        void move() {
            x += dx;
            y += dy;
        }

        void reflect(char mirrorType) {
            if (mirrorType == '/') {
                int oldDx = dx;
                dx = -dy;
                dy = -oldDx;
            } else if (mirrorType == '\\') {
                int oldDy = dy;
                dy = dx;
                dx = oldDy;
            }
        }
    }

}
