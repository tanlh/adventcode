package com.alibaba.logistics.station;

import java.io.*;
import java.util.*;

public class Day14 {

    private static final char ZERO = 'O';
    private static final char BLOCK = '#';
    private static final char EMPTY = '.';

    public static void main(String[] args) {
        long numberOfCycles = 1000000000L; // Large number of cycles

        try {
            // Read the grid from the file
            File file = new File("file.txt");
            List<char[]> gridList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    gridList.add(line.toCharArray());
                }
            }

            // Convert list to 2D array
            char[][] grid = new char[gridList.size()][];
            grid = gridList.toArray(grid);

            // Store grid configurations to detect cycles
            Map<String, Integer> seenConfigurations = new HashMap<>();
            String gridString = gridToString(grid);
            seenConfigurations.put(gridString, 0);

            // Run simulation until a cycle is detected
            long cycleLength = 0;
            long cyclesCompleted = 0;
            boolean cycleDetected = false;
            for (long i = 0; i < numberOfCycles; i++) {
                moveZeros(grid, "UP");
                moveZeros(grid, "LEFT");
                moveZeros(grid, "DOWN");
                moveZeros(grid, "RIGHT");

                gridString = gridToString(grid);
                if (seenConfigurations.containsKey(gridString)) {
                    // Cycle detected
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
                    moveZeros(grid, "UP");
                    moveZeros(grid, "LEFT");
                    moveZeros(grid, "DOWN");
                    moveZeros(grid, "RIGHT");
                }
            }

            // Calculate the sum of the values of 'O's after steady state
            long sum = calculateSumOfValues(grid);

            // Print the result
            printGrid(grid);

            // Print the sum of the values
            System.out.println("Sum of the values of 'O's after " + numberOfCycles + " cycle(s): " + sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert grid to string for comparison and map storage
    private static String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(new String(row));
        }
        return sb.toString();
    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            System.out.println(new String(row));
        }
    }

    private static void moveZeros(char[][] grid, String direction) {
        int rows = grid.length;
        int cols = grid[0].length;

        switch (direction) {
            case "UP":
                for (int col = 0; col < cols; col++) {
                    int writeRow = 0;
                    for (int row = 0; row < rows; row++) {
                        if (grid[row][col] == ZERO) {
                            if (row != writeRow) {
                                grid[writeRow][col] = ZERO;
                                grid[row][col] = EMPTY;
                            }
                            writeRow++;
                        } else if (grid[row][col] == BLOCK) {
                            writeRow = row + 1;
                        }
                    }
                }
                break;
            case "LEFT":
                for (int row = 0; row < rows; row++) {
                    int writeCol = 0;
                    for (int col = 0; col < cols; col++) {
                        if (grid[row][col] == ZERO) {
                            if (col != writeCol) {
                                grid[row][writeCol] = ZERO;
                                grid[row][col] = EMPTY;
                            }
                            writeCol++;
                        } else if (grid[row][col] == BLOCK) {
                            writeCol = col + 1;
                        }
                    }
                }
                break;
            case "DOWN":
                for (int col = 0; col < cols; col++) {
                    int writeRow = rows - 1;
                    for (int row = rows - 1; row >= 0; row--) {
                        if (grid[row][col] == ZERO) {
                            if (row != writeRow) {
                                grid[writeRow][col] = ZERO;
                                grid[row][col] = EMPTY;
                            }
                            writeRow--;
                        } else if (grid[row][col] == BLOCK) {
                            writeRow = row - 1;
                        }
                    }
                }
                break;
            case "RIGHT":
                for (int row = 0; row < rows; row++) {
                    int writeCol = cols - 1;
                    for (int col = cols - 1; col >= 0; col--) {
                        if (grid[row][col] == ZERO) {
                            if (col != writeCol) {
                                grid[row][writeCol] = ZERO;
                                grid[row][col] = EMPTY;
                            }
                            writeCol--;
                        } else if (grid[row][col] == BLOCK) {
                            writeCol = col - 1;
                        }
                    }
                }
                break;
        }
    }

    private static int calculateSumOfValues(char[][] grid) {
        int sum = 0;
        int rows = grid.length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == ZERO) {
                    // The value is the distance from the bottom row
                    sum += (rows - row);
                }
            }
        }
        return sum;
    }
}
