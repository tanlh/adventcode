package _2023;

import util.Util;

import java.util.HashMap;
import java.util.Map;

public class Day14 {

    public static void main(String[] args) {
        long numberOfCycles = 1000000000L;
        char[][] grid = Util.readFileToGrid();

        // Store grid configurations to detect cycles
        Map<String, Integer> seenConfigurations = new HashMap<>();
        var gridString = gridToString(grid);
        seenConfigurations.put(gridString, 0);

        var cycleLength = 0L;
        var cyclesCompleted = 0L;
        var cycleDetected = false;
        for (long i = 0; i < numberOfCycles; i++) {
            moveCircle(grid);

            gridString = gridToString(grid);
            if (seenConfigurations.containsKey(gridString)) {
                cycleDetected = true;
                cycleLength = i + 1 - seenConfigurations.get(gridString);
                cyclesCompleted = i + 1;
                break;
            } else {
                seenConfigurations.put(gridString, (int) (i + 1));
            }
        }

        if (cycleDetected) {
            long remainingCycles = (numberOfCycles - cyclesCompleted) % cycleLength;
            for (long i = 0; i < remainingCycles; i++) {
                moveCircle(grid);
            }
        }

        var sum = calculateSumOfValues(grid);
        System.out.println("Sum load: " + sum);
    }

    private static String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(new String(row));
        }
        return sb.toString();
    }

    private static void moveCircle(char[][] grid) {
        moveGrid(grid, -1, 0); // Move UP
        moveGrid(grid, 0, -1); // Move LEFT
        moveGrid(grid, 1, 0); // Move DOWN
        moveGrid(grid, 0, 1); // Move RIGHT
    }

    private static void moveGrid(char[][] grid, int rowDelta, int colDelta) {
        int rows = grid.length;
        int cols = grid[0].length;
        int startRow = (rowDelta > 0) ? rows - 1 : 0;
        int endRow = (rowDelta > 0) ? -1 : rows;
        int startCol = (colDelta > 0) ? cols - 1 : 0;
        int endCol = (colDelta > 0) ? -1 : cols;

        for (int row = startRow; row != endRow; row += (rowDelta > 0 ? -1 : 1)) {
            for (int col = startCol; col != endCol; col += (colDelta > 0 ? -1 : 1)) {
                if (grid[row][col] == '0') {
                    int nextRow = row;
                    int nextCol = col;
                    // Find next position
                    while (isValidPosition(nextRow + rowDelta, nextCol + colDelta, rows, cols) &&
                        grid[nextRow + rowDelta][nextCol + colDelta] == '.') {
                        nextRow += rowDelta;
                        nextCol += colDelta;
                    }
                    // Move ZERO if needed
                    if (nextRow != row || nextCol != col) {
                        grid[nextRow][nextCol] = '0';
                        grid[row][col] = '.';
                    }
                } else if (grid[row][col] == '#') {
                    // Skip blocked positions
                    break;
                }
            }
        }

    }

    private static boolean isValidPosition(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private static int calculateSumOfValues(char[][] grid) {
        int sum = 0;
        int rows = grid.length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '0') {
                    sum += (rows - row);
                }
            }
        }
        return sum;
    }
}
