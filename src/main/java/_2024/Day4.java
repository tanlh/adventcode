package _2024;

import util.Util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

public class Day4 {

    private static final int[][] DIRECTIONS = {
        {0, -1}, {0, 1}, {-1, 0}, {1, 0},
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };
    private static final String XMAS = "XMAS";
    private static final Set<String> CROSS_MAS = Set.of("MAS", "SAM");

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();
        var part1 = 0;
        var part2 = 0;

        for (var i = 0; i < grid.length; i++) {
            for (var j = 0; j < grid[0].length; j++) {
                part1 += countXmas(grid, i, j);
                part2 += isCrossMas(grid, i, j);
            }
        }

        System.err.println("Part 1: " + part1);
        System.err.println("Part 2: " + part2);
    }

    private static int countXmas(char[][] grid, int row, int col) {
        return (int) Arrays.stream(DIRECTIONS)
            .filter(dir -> grid[row][col] == 'X' && isXmas(grid, row, col, dir))
            .count();
    }

    private static boolean isXmas(char[][] grid, int startRow, int startCol, int[] dir) {
        return IntStream.range(0, XMAS.length())
            .allMatch(i -> {
                var row = startRow + i * dir[0];
                var col = startCol + i * dir[1];
                return Util.isInGrid(grid, row, col) && grid[row][col] == XMAS.charAt(i);
            });
    }

    private static int isCrossMas(char[][] grid, int row, int col) {
        var currentChar = grid[row][col];
        if (currentChar != 'A') {
            return 0;
        }

        var word1 = String.valueOf(Util.isInGrid(grid, row - 1, col - 1) ? grid[row - 1][col - 1] : "") +
            currentChar +
            (Util.isInGrid(grid, row + 1, col + 1) ? grid[row + 1][col + 1] : "");
        var word2 = String.valueOf(Util.isInGrid(grid, row - 1, col + 1) ? grid[row - 1][col + 1] : "") +
            currentChar +
            (Util.isInGrid(grid, row + 1, col - 1) ? grid[row + 1][col - 1] : "");

        return CROSS_MAS.contains(word1) && CROSS_MAS.contains(word2) ? 1 : 0;
    }

}
