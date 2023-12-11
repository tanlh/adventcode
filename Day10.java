import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.*;

public class Day10 {

    // Arrays to represent the change in coordinates when moving in each of the four directions
    private static final int[] dx = {0, 1, 0, -1}; // Changes in the row index for up, right, down, left
    private static final int[] dy = {1, 0, -1, 0}; // Changes in the column index for up, right, down, left

    public static void main(String[] args) {
        var lines = readFileToLines();
        char[][] grid = new char[lines.size()][lines.get(0).length()];

        var startRow = 0;
        var startCol = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                var currChar = lines.get(i).charAt(j);
                grid[i][j] = currChar;
                if (currChar == 'S') {
                    startRow = i;
                    startCol = j;
                }
            }
        }

        var loopPoints = findLoop(grid, startRow, startCol);
        System.err.println("Farthest point: " + loopPoints.size() / 2);

        var markedGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                markedGrid[i][j] = loopPoints.contains(Pair.of(i, j));
            }
        }

        var area = findEnclosedArea(markedGrid, loopPoints);
        System.err.println("Areas " + area);
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

    private static Set<Pair<Integer, Integer>> findLoop(char[][] grid, int startRow, int startCol) {
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

        Set<Pair<Integer, Integer>> loopPoints = new LinkedHashSet<>();
        loopPoints.add(Pair.of(startRow, startCol));

        do {
            loopPoints.add(Pair.of(currRow, currCol));

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

            prevRow = currRow;
            prevCol = currCol;
            currRow = rowTemp;
            currCol = colTemp;
        } while (currRow != startRow || currCol != startCol);

        return loopPoints;
    }

    private static int findEnclosedArea(boolean[][] grid, Set<Pair<Integer, Integer>> wallPoints) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // Mark the wall points as visited
        for (Pair<Integer, Integer> wp : wallPoints) {
            visited[wp.getLeft()][wp.getRight()] = true;
        }

        // Convert the wallPoints to a list to access by index
        List<Pair<Integer, Integer>> wallPointsList = new ArrayList<>(wallPoints);

        // Find a point inside the wall to start
        Pair<Integer, Integer> insidePoint = getInsidePoint(wallPointsList, rows, cols);
        if (insidePoint == null) {
            return 0; // No inside point found, no enclosed area
        }

        // Perform flood fill from the found inside point
        return floodFill(grid, visited, insidePoint.getLeft(), insidePoint.getRight());
    }

    private static Pair<Integer, Integer> getInsidePoint(List<Pair<Integer, Integer>> wallPointsList, int rows, int cols) {
        for (int i = 0; i < wallPointsList.size(); i++) {
            Pair<Integer, Integer> current = wallPointsList.get(i);
            Pair<Integer, Integer> next = wallPointsList.get((i + 1) % wallPointsList.size());
            // Create a point to the left of the midpoint of the current wall segment
            int midX = (current.getLeft() + next.getLeft()) / 2;
            int midY = (current.getRight() + next.getRight()) / 2;
            Pair<Integer, Integer> testPoint = Pair.of(midX - 1, midY);
            if (testPoint.getLeft() >= 0 && !isWall(testPoint, wallPointsList)) {
                return testPoint;
            }
        }
        return null;
    }

    private static boolean isWall(Pair<Integer, Integer> point, List<Pair<Integer, Integer>> wallPointsList) {
        return wallPointsList.contains(point);
    }

    private static int floodFill(boolean[][] grid, boolean[][] visited, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || visited[x][y] || grid[x][y]) {
            return 0;
        }
        visited[x][y] = true;
        int area = 1;
        for (int i = 0; i < dx.length; i++) {
            area += floodFill(grid, visited, x + dx[i], y + dy[i]);
        }
        return area;
    }

}
