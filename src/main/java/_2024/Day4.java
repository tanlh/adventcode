package _2024;

import org.apache.commons.lang3.StringUtils;
import util.Util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.IntStream;

import static util.Constants.*;

public class Day4 {

    private static final String XMAS = "XMAS";
    private static final Set<String> CROSS_MAS = Set.of("MAS", "SAM");
    private static char[][] grid;

    public static void main(String[] args) {
        grid = Util.readFileToGrid();
        var part1 = 0;
        var part2 = 0;

        for (var i = 0; i < grid.length; i++) {
            for (var j = 0; j < grid[0].length; j++) {
                part1 += countXmas(i, j);
                part2 += isCrossMas(i, j);
            }
        }

        System.err.println("Part 1: " + part1);
        System.err.println("Part 2: " + part2);
    }

    private static int countXmas(int row, int col) {
        return (int) Arrays.stream(FULL_DIRECTIONS)
            .filter(dir -> grid[row][col] == 'X' && isXmas(row, col, dir))
            .count();
    }

    private static boolean isXmas(int startRow, int startCol, int[] dir) {
        return IntStream.range(0, XMAS.length())
            .allMatch(i -> {
                var row = startRow + i * dir[0];
                var col = startCol + i * dir[1];
                return Util.isInGrid(grid, row, col) && grid[row][col] == XMAS.charAt(i);
            });
    }

    private static int isCrossMas(int row, int col) {
        var currentChar = grid[row][col];
        if (currentChar != 'A') return 0;

        var word1 = buildWord(row, col, TOP_LEFT, BOTTOM_RIGHT);
        var word2 = buildWord(row, col, TOP_RIGHT, BOTTOM_LEFT);
        return CROSS_MAS.contains(word1) && CROSS_MAS.contains(word2) ? 1 : 0;
    }

    private static String buildWord(int row, int col, int[] direction1, int[] direction2) {
        return String.valueOf(Util.isInGrid(grid, row + direction1[1], col + direction1[0]) ? grid[row + direction1[1]][col + direction1[0]] : StringUtils.EMPTY) +
            grid[row][col] +
            (Util.isInGrid(grid, row + direction2[1], col + direction2[0]) ? grid[row + direction2[1]][col + direction2[0]] : StringUtils.EMPTY);
    }

}
