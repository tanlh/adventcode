import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day10 {

    @Data
    @AllArgsConstructor
    public static class Point {
        private int row;
        private int col;
        @EqualsAndHashCode.Exclude
        private char symbol;
        @EqualsAndHashCode.Exclude
        private int rowDiff;
        @EqualsAndHashCode.Exclude
        private int colDiff;

        public static Point of(int row, int col) {
            return new Point(row, col, ' ', 0, 0);
        }
    }

    // Arrays to represent the change in coordinates when moving in each of the four directions
    private static final int[] dx = {0, 1, 0, -1}; // Changes in the row index for right, down, left, up
    private static final int[] dy = {1, 0, -1, 0}; // Changes in the column index for right, down, left, up

    public static void main(String[] args) {
        var lines = readFileToLines();
        var rows = lines.size();
        var cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        var startRow = 0;
        var startCol = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                var currChar = lines.get(i).charAt(j);
                grid[i][j] = currChar;
                if (currChar == 'S') {
                    startRow = i;
                    startCol = j;
                }
            }
        }

        // PART 1
        var piles = findPiles(grid, startRow, startCol);
        System.err.println("Farthest point: " + piles.size() / 2);

        // PART 2
        var markedGrid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                markedGrid[i][j] = piles.contains(Point.of(i, j)) ? 1 : 0;
            }
        }

        markAdjacent(rows, cols, piles, markedGrid);

        print2DArray(markedGrid);

        int area = floodFillCount(markedGrid);
        int otherArea = rows * cols - area - piles.size();
        System.err.println("Area: " + area);
        System.err.println("Other: " + otherArea);
    }

    public static void print2DArray(int[][] array) {
        System.err.println(array.length + " " + array[0].length);
        for (int[] row : array) {
            for (int item : row) {
                System.out.print(item + " ");
            }
            System.out.println(); // Newline after each row
        }
    }

    // This is the hack that my friend told me
    // With the piles in hand, we go through each cell and mark the cell on the left of it (in the current direction)
    // After that we can start the flood fill from these marked cell. It doesn't matter the marked cell is inside or outside the piles
    private static void markAdjacent(int rows, int cols, List<Point> piles, int[][] markedGrid) {
        // Don't know how to fill the adjacent for the S
        // Need manual input here for the S - Damn :))))
        // Luckily, all the samples and file input doesn't have the case that surrounding S is not a piles cell

        for (int i = 1; i < piles.size(); i++) {
            var curr = piles.get(i);
            if (curr.symbol == '|') {
                if (curr.rowDiff == 1) {
                    if (curr.col + 1 < cols && markedGrid[curr.row][curr.col + 1] == 0) {
                        markedGrid[curr.row][curr.col + 1] = 2;
                    }
                } else {
                    if (curr.col - 1 >= 0 && markedGrid[curr.row][curr.col - 1] == 0) {
                        markedGrid[curr.row][curr.col - 1] = 2;
                    }
                }
            }
            if (curr.symbol == '-') {
                if (curr.colDiff == 1) {
                    if (curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col] == 0) {
                        markedGrid[curr.row - 1][curr.col] = 2;
                    }
                } else {
                    if (curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col] == 0) {
                        markedGrid[curr.row + 1][curr.col] = 2;
                    }
                }
            }
            if (curr.symbol == '7') {
                if (curr.colDiff == -1) {
                    if (curr.col - 1 >= 0 && curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col - 1] == 0) {
                        markedGrid[curr.row + 1][curr.col - 1] = 2;
                    }
                    if (curr.col - 1 >= 0 && markedGrid[curr.row][curr.col - 1] == 0) {
                        markedGrid[curr.row][curr.col - 1] = 2;
                    }
                    if (curr.row + 1 >= 0 && markedGrid[curr.row + 1][curr.col] == 0) {
                        markedGrid[curr.row + 1][curr.col] = 2;
                    }
                } else {
                    if (curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col] == 0) {
                        markedGrid[curr.row - 1][curr.col] = 2;
                    }
                    if (curr.row - 1 >= 0 && curr.col + 1 < cols && markedGrid[curr.row - 1][curr.col + 1] == 0) {
                        markedGrid[curr.row - 1][curr.col + 1] = 2;
                    }
                    if (curr.col + 1 < cols && markedGrid[curr.row][curr.col + 1] == 0) {
                        markedGrid[curr.row][curr.col + 1] = 2;
                    }
                }
            }
            if (curr.symbol == 'F') {
                if (curr.rowDiff == 1) {
                    if (curr.col + 1 < cols && curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col + 1] == 0) {
                        markedGrid[curr.row + 1][curr.col + 1] = 2;
                    }
                    if (curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col] == 0) {
                        markedGrid[curr.row + 1][curr.col] = 2;
                    }
                    if (curr.col + 1 < cols && markedGrid[curr.row][curr.col + 1] == 0) {
                        markedGrid[curr.row][curr.col + 1] = 2;
                    }
                } else {
                    if (curr.col - 1 >= 0 && markedGrid[curr.row][curr.col - 1] == 0) {
                        markedGrid[curr.row][curr.col - 1] = 2;
                    }
                    if (curr.row - 1 >= 0 && curr.col - 1 >= 0 && markedGrid[curr.row - 1][curr.col - 1] == 0) {
                        markedGrid[curr.row - 1][curr.col - 1] = 2;
                    }
                    if (curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col] == 0) {
                        markedGrid[curr.row - 1][curr.col] = 2;
                    }
                }
            }
            if (curr.symbol == 'L') {
                if (curr.colDiff == 1) {
                    if (curr.col + 1 < cols && curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col + 1] == 0) {
                        markedGrid[curr.row - 1][curr.col + 1] = 2;
                    }
                    if (curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col] == 0) {
                        markedGrid[curr.row - 1][curr.col] = 2;
                    }
                    if (curr.col + 1 < cols && markedGrid[curr.row][curr.col + 1] == 0) {
                        markedGrid[curr.row][curr.col + 1] = 2;
                    }
                } else {
                    if (curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col] == 0) {
                        markedGrid[curr.row + 1][curr.col] = 2;
                    }
                    if (curr.row + 1 < rows && curr.col - 1 >= 0 && markedGrid[curr.row + 1][curr.col - 1] == 0) {
                        markedGrid[curr.row + 1][curr.col - 1] = 2;
                    }
                    if (curr.col - 1 >= 0 && markedGrid[curr.row][curr.col - 1] == 0) {
                        markedGrid[curr.row][curr.col - 1] = 2;
                    }
                }
            }
            if (curr.symbol == 'J') {
                if (curr.rowDiff == -1) {
                    if (curr.col - 1 >= 0 && curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col - 1] == 0) {
                        markedGrid[curr.row - 1][curr.col - 1] = 2;
                    }
                    if (curr.col - 1 >= 0 && markedGrid[curr.row][curr.col - 1] == 0) {
                        markedGrid[curr.row][curr.col - 1] = 2;
                    }
                    if (curr.row - 1 >= 0 && markedGrid[curr.row - 1][curr.col] == 0) {
                        markedGrid[curr.row - 1][curr.col] = 2;
                    }
                } else {
                    if (curr.row + 1 < rows && markedGrid[curr.row + 1][curr.col] == 0) {
                        markedGrid[curr.row + 1][curr.col] = 2;
                    }
                    if (curr.row + 1 < rows && curr.col + 1 < cols && markedGrid[curr.row + 1][curr.col + 1] == 0) {
                        markedGrid[curr.row + 1][curr.col + 1] = 2;
                    }
                    if (curr.col + 1 < cols && markedGrid[curr.row][curr.col + 1] == 0) {
                        markedGrid[curr.row][curr.col + 1] = 2;
                    }
                }
            }
        }
    }

    private static List<String> readFileToLines() {
        List<String> lines = new ArrayList<>();

        try (var scanner = new Scanner(new File("file.txt"))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (Exception ignored) {
        }
        return lines;
    }

    private static List<Point> findPiles(char[][] grid, int startRow, int startCol) {
        int prevRow = startRow;
        int prevCol = startCol;
        int currRow = 0;
        int currCol = 0;

        // Find the first direction from starting point
        if (startRow - 1 >= 0 && List.of('|', '7', 'F').contains(grid[startRow - 1][startCol])) {
            currRow = startRow - 1;
            currCol = startCol;
        } else if (startRow + 1 < grid.length && List.of('|', 'L', 'J').contains(grid[startRow + 1][startCol])) {
            currRow = startRow + 1;
            currCol = startCol;
        } else if (startCol - 1 >= 0 && List.of('-', 'L', 'F').contains(grid[startRow][startCol - 1])) {
            currRow = startRow;
            currCol = startCol - 1;
        } else if (startCol + 1 < grid[0].length && List.of('-', 'J', '7').contains(grid[startRow][startCol + 1])) {
            currRow = startRow;
            currCol = startCol + 1;
        }

        List<Point> piles = new ArrayList<>();
        piles.add(new Point(startRow, startCol, 'S', currRow - startRow, currCol - startCol));

        do {
            var currPipe = grid[currRow][currCol];
            var rowTemp = currRow;
            var colTemp = currCol;

            if (currPipe == '-') { // Up/Down
                colTemp += currCol - prevCol;
            } else if (currPipe == '|') { // Left/Right
                rowTemp += currRow - prevRow;
            } else if (currPipe == 'L' || currPipe == '7') { // Top-Left/Bottom-Right
                rowTemp += currCol - prevCol;
                colTemp += currRow - prevRow;
            } else if (currPipe == 'J' || currPipe == 'F') { // Top-Right/Bottom-Left
                rowTemp -= currCol - prevCol;
                colTemp -= currRow - prevRow;
            }

            piles.add(new Point(currRow, currCol, grid[currRow][currCol], rowTemp - currRow, colTemp - currCol));
            prevRow = currRow;
            prevCol = currCol;
            currRow = rowTemp;
            currCol = colTemp;
        } while (currRow != startRow || currCol != startCol);

        return piles;
    }

    private static int floodFillCount(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        // Start flood fill for each cell that are already marked
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2 && !visited[i][j]) {
                    count += floodFill(grid, visited, i, j);
                }
            }
        }

        return count;
    }

    // Flood fill algorithm
    private static int floodFill(int[][] grid, boolean[][] visited, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 1 || visited[x][y]) {
            return 0;
        }

        visited[x][y] = true;  // Mark the cell as visited

        int area = 1;
        for (int dir = 0; dir < 4; dir++) {
            area += floodFill(grid, visited, x + dx[dir], y + dy[dir]);
        }

        return area;
    }

}
