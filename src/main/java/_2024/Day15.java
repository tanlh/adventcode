package _2024;

import util.Point;
import util.Util;

import java.util.stream.IntStream;

import static util.Constants.DIRMOVEMAP;

public class Day15 {

    private static char[][] grid;
    private static Point robot;

    public static void main(String[] args) {
        var input = Util.readFileToBlocks();
        var moves = String.join("", input.get(1));
        grid = input.getFirst().stream().map(String::toCharArray).toArray(char[][]::new);
        robot = findRobotStart(grid);

        moves.chars().forEach(move -> moveRobot((char) move));
        System.err.println("Part 1: " + calculateGPSSum());
    }

    private static Point findRobotStart(char[][] grid) {
        return IntStream.range(0, grid.length).boxed()
            .flatMap(i -> IntStream.range(0, grid[0].length)
                .filter(j -> grid[i][j] == '@')
                .mapToObj(j -> new Point(j, i)))
            .findFirst().orElseThrow();
    }

    private static void moveRobot(char move) {
        var dir = DIRMOVEMAP.get(move);
        int dirX = dir[0], dirY = dir[1];
        int newX = robot.x + dirX, newY = robot.y + dirY;

        if (canMove(newX, newY, dir)) {
            var boxCount = (int) IntStream.iterate(0,
                i -> Util.isInGrid(grid, newY + i * dirY, newX + i * dirX) && grid[newY + i * dirY][newX + i * dirX] == 'O',
                i -> i + 1
            ).count() + 1;

            for (var i = boxCount; i > 0; i--) {
                grid[robot.y + i * dirY][robot.x + i * dirX] = 'O';
            }

            grid[robot.y][robot.x] = '.';
            robot.x = newX;
            robot.y = newY;
            grid[robot.y][robot.x] = '@';
        }
    }

    private static boolean canMove(int x, int y, int[] dir) {
        return Util.isInGrid(grid, y, x) && grid[y][x] != '#' && (grid[y][x] == '.' || canMove(x + dir[0], y + dir[1], dir));
    }

    private static long calculateGPSSum() {
        return IntStream.range(0, grid.length)
            .flatMap(y -> IntStream.range(0, grid[y].length)
                .filter(x -> grid[y][x] == 'O')
                .map(x -> y * 100 + x))
            .sum();
    }

}
