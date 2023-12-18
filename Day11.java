package com.alibaba.logistics.station;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {
        var grid = Util.readFileToGrid();

        List<Integer> emptyRows = new ArrayList<>();
        List<Integer> emptyCols = new ArrayList<>();
        List<Pair<Integer, Integer>> galaxies = new ArrayList<>();
        traversalGrid(grid, emptyRows, emptyCols, galaxies);

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

    private static void traversalGrid(char[][] grid, List<Integer> emptyRows, List<Integer> emptyCols, List<Pair<Integer, Integer>> galaxies) {
        var rows = grid.length;
        var cols = grid[0].length;
        var rowsOnlyDots = new Boolean[rows];
        var colsOnlyDots = new Boolean[cols];
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
                emptyRows.add(i);
            }
        }

        for (int j = 0; j < cols; j++) {
            if (colsOnlyDots[j]) {
                emptyCols.add(j);
            }
        }
    }
}
