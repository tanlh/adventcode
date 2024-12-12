package _2024;

import util.Point;
import util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static util.Constants.DIRECTIONS;

public class Day12 {

    record Region(char plantType, int area, int perimeter) {
        long getPrice() {
            return (long) area * perimeter;
        }
    }

    private static char[][] grid;
    private static boolean[][] visited;
    private static final List<Region> regions = new LinkedList<>();

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        visited = new boolean[grid.length][grid[0].length];
        var totalPrice = 0L;

        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[0].length; x++) {
                if (!visited[y][x]) {
                    var region = floodFill(x, y);
                    regions.add(region);
                    totalPrice += region.getPrice();
                }
            }
        }
        System.out.println("Result: " + totalPrice);
    }

    private static Region floodFill(int startX, int startY) {
        var plantType = grid[startY][startX];
        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(startX, startY));
        visited[startY][startX] = true;

        int area = 0, perimeter = 0;

        while (!queue.isEmpty()) {
            var current = queue.poll();
            area++;

            for (var dir : DIRECTIONS) {
                var newX = current.x + dir[0];
                var newY = current.y + dir[1];

                if (!Util.isInGrid(grid, newY, newX) || grid[newY][newX] != plantType) {
                    perimeter++;
                } else if (!visited[newY][newX]) {
                    queue.offer(new Point(newX, newY));
                    visited[newY][newX] = true;
                }
            }
        }

        return new Region(plantType, area, perimeter);
    }

}
