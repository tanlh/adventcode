package com.alibaba.logistics.station;

import org.apache.commons.lang3.tuple.Pair;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        char[][] grid = readFileToGrid();

        List<Integer> emptyRows = new ArrayList<>();
        List<Integer> emptyCols = new ArrayList<>();
        List<Pair<Integer, Integer>> galaxies = new ArrayList<>();
        traversalGrid(grid, emptyRows, emptyCols, galaxies);

        System.err.println(emptyRows);
        System.err.println(emptyCols);
        System.err.println(galaxies);

        var sum = 0L;

        for (int i = 0; i < galaxies.size() - 1; i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                var g1 = galaxies.get(i);
                var g2 = galaxies.get(j);
                var minRow = Math.min(g1.getLeft(), g2.getLeft());
                var maxRow = Math.max(g1.getLeft(), g2.getLeft());
                var minCol = Math.min(g1.getRight(), g2.getRight());
                var maxCol = Math.max(g1.getRight(), g2.getRight());
                sum += maxRow - minRow + maxCol - minCol;
                for (var er : emptyRows) {
                    if (er > minRow && er < maxRow) sum += (1000000 - 1);
                }
                for (var ec : emptyCols) {
                    if (ec > minCol && ec < maxCol) sum += (1000000 - 1);
                }
            }
        }

        System.err.println("Sum: " + sum);
    }

    private static void traversalGrid(char[][] grid, List<Integer> doubleRows, List<Integer> doubleCols, List<Pair<Integer, Integer>> galaxies) {
        int rows = grid.length;
        int cols = grid[0].length;

        Boolean[] rowsOnlyDots = new Boolean[rows];
        Boolean[] colsOnlyDots = new Boolean[cols];
        Arrays.fill(rowsOnlyDots, true);
        Arrays.fill(colsOnlyDots, true);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '#') {
                    galaxies.add(Pair.of(i, j));
                    rowsOnlyDots[i] = false;
                    colsOnlyDots[j] = false;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            if (rowsOnlyDots[i]) {
                doubleRows.add(i);
            }
        }

        for (int j = 0; j < cols; j++) {
            if (colsOnlyDots[j]) {
                doubleCols.add(j);
            }
        }
    }

    private static char[][] readFileToGrid() {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader("file.txt"))) {
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
        } catch (Exception ignored) {}

        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

}
