package _2023;

import util.Util;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Day16 {

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();
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

        System.out.println("Result: " + maxEnergized);
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

    @AllArgsConstructor
    static class Beam {
        int x, y, dx, dy;

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
            this.move();
        }
    }

}
