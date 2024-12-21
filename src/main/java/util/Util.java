package util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class Util {

    @SneakyThrows
    public List<String> readFileToLines() {
        return Files.readAllLines(Paths.get("input.txt"));
    }

    public List<List<String>> readFileToBlocks() {
        List<List<String>> blocks = new ArrayList<>();

        var lines = readFileToLines();
        List<String> currentBlock = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty()) {
                if (!currentBlock.isEmpty()) {
                    blocks.add(new ArrayList<>(currentBlock));
                    currentBlock.clear();
                }
            } else {
                currentBlock.add(line);
            }
        }

        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock);
        }

        return blocks;
    }

    public char[][] readFileToGrid() {
        var lines = readFileToLines();
        var grid = new char[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    public int[][] readFileToGridInt() {
        var lines = readFileToLines();
        var cols = lines.getFirst().length();
        var grid = new int[lines.size()][cols];
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }

        return grid;
    }

    public List<String> splitLine(String line, String splitter) {
        return parseLine(line, splitter, Function.identity());
    }

    public <T> List<T> parseLine(String line, String splitter, Function<String, T> mapping) {
        return Arrays.stream(line.split(splitter))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .map(mapping)
            .toList();
    }

    public long findLCM(List<Integer> numbers) {
        long lcm = numbers.getFirst();
        for (int i = 1; i < numbers.size(); i++) {
            lcm = calculateLCM(lcm, numbers.get(i));
        }
        return lcm;
    }

    private long calculateLCM(long a, long b) {
        return (a * b) / calculateGCD(a, b);
    }

    private long calculateGCD(long a, long b) {
        return b == 0 ? a : calculateGCD(b, a % b);
    }

    public void printGrid(List<Point> points, int rows, int cols) {
        char[][] grid = new char[rows][cols];
        Arrays.stream(grid).forEach(row -> Arrays.fill(row, '.'));
        points.forEach(p -> grid[p.y][p.x] = '#');
        Arrays.stream(grid)
            .map(String::new)
            .forEach(System.out::println);
    }

    public List<Range> mergeRanges(List<Range> ranges) {
        List<Range> sortedRanges = new ArrayList<>(ranges);
        sortedRanges.sort(Comparator.comparing(Range::getMin));

        List<Range> mergedRanges = new ArrayList<>();
        var last = sortedRanges.getFirst();

        for (int i = 1; i < sortedRanges.size(); i++) {
            var current = sortedRanges.get(i);
            if (last.overlaps(current)) {
                last = last.merge(current);
            } else {
                mergedRanges.add(last);
                last = current;
            }
        }

        mergedRanges.add(last);

        return mergedRanges;
    }

    public boolean isInGrid(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    public boolean isInGrid(int[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    public <T> List<List<T>> generateCombinations(List<List<T>> dataSet) {
        List<List<T>> allCombinations = new ArrayList<>();
        generateCombinations(dataSet, new ArrayList<>(), 0, allCombinations);
        return allCombinations;
    }

    private <T> void generateCombinations(List<List<T>> dataSet, List<T> currentCombination, int index, List<List<T>> combinations) {
        if (index == dataSet.size()) {
            combinations.add(new ArrayList<>(currentCombination));
            return;
        }

        List<T> currentCollection = dataSet.get(index);
        for (var item : currentCollection) {
            currentCombination.add(item);
            generateCombinations(dataSet, currentCombination, index + 1, combinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

}
