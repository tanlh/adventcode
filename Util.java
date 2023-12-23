package com.alibaba.logistics.station;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class Util {

    @SneakyThrows
    public List<String> readFileToLines() {
        return Files.readAllLines(Paths.get("file.txt"));
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
        var grid = new char[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
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
            .collect(Collectors.toList());
    }

    public long findLCM(List<Integer> numbers) {
        long lcm = numbers.get(0);
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

}
