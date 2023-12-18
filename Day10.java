package com.alibaba.logistics.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

public class Day10 {

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    public static void main(String[] args) {
        var lines = Util.readFileToLines();
        var rows = lines.size();
        var cols = lines.get(0).length();
        var grid = new char[rows][cols];

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
        var area = floodFillCount(markedGrid);
        var otherArea = rows * cols - area - piles.size();
        System.err.println("Area: " + area);
        System.err.println("Other: " + otherArea);
    }

    // This is the hack that my friend told me
    // With the piles in hand, we go through each cell and mark the cell on the left of it (in the current direction)
    // After that we can start the flood fill from these marked cell. It doesn't matter the marked cell is inside or outside the piles
    private static void markAdjacent(int rows, int cols, List<Point> piles, int[][] markedGrid) {
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

            switch (currPipe) {
                case '-':
                    colTemp += currCol - prevCol;
                    break;
                case '|':
                    rowTemp += currRow - prevRow;
                    break;
                case 'L':
                case '7':
                    rowTemp += currCol - prevCol;
                    colTemp += currRow - prevRow;
                    break;
                case 'J':
                case 'F':
                    rowTemp -= currCol - prevCol;
                    colTemp -= currRow - prevRow;
                    break;
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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2 && !visited[i][j]) {
                    count += floodFill(grid, visited, i, j);
                }
            }
        }

        return count;
    }

    private static int floodFill(int[][] grid, boolean[][] visited, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 1 || visited[x][y]) {
            return 0;
        }

        visited[x][y] = true;

        int area = 1;
        for (int dir = 0; dir < 4; dir++) {
            area += floodFill(grid, visited, x + dx[dir], y + dy[dir]);
        }

        return area;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"symbol", "rowDiff", "colDiff"})
    static class Point {
        int row;
        int col;
        char symbol;
        int rowDiff;
        int colDiff;

        public static Point of(int row, int col) {
            return new Point(row, col, ' ', 0, 0);
        }
    }

}
