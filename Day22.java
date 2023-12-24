package com.alibaba.logistics.station;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22 {

    public static void main(String[] args) {
        var lines = Util.readFileToLines();
        var bricks = IntStream.range(0, lines.size())
            .mapToObj(i -> new Brick((char) ('A' + i), lines.get(i)))
            .collect(Collectors.toList());

        simulateFalling(bricks);
        buildSupportGraph(bricks);

        var safeDisBricks = bricks.stream()
            .peek(brick -> brick.safeToRemove = brick.supporting.stream()
                .allMatch(supporting -> supporting.supporters.size() > 1))
            .filter(brick -> brick.safeToRemove)
            .count();
        System.err.println("Part 1: " + safeDisBricks);

        var fallenBricks = countFallenBricks(bricks);
        System.err.println("Part 2: " + fallenBricks);
    }

    private static void simulateFalling(List<Brick> bricks) {
        bricks.sort(Comparator.comparingInt(b -> b.startZ));

        Map<Point, Integer> highestZMap = new HashMap<>();
        for (var brick : bricks) {
            var highestZ = 0;
            List<Point> coveredPositions = new ArrayList<>();

            for (int x = brick.startX; x <= brick.endX; x++) {
                for (int y = brick.startY; y <= brick.endY; y++) {
                    Point position = new Point(x, y);
                    coveredPositions.add(position);
                    highestZ = Math.max(highestZ, highestZMap.computeIfAbsent(position, p -> 0));
                }
            }

            var fallDistance = Math.max(0, brick.startZ - highestZ - 1);
            brick.startZ -= fallDistance;
            brick.endZ -= fallDistance;
            coveredPositions.forEach(position -> highestZMap.put(position, brick.endZ));
        }
    }

    private static void buildSupportGraph(List<Brick> bricks) {
        for (var brick : bricks) {
            for (var other : bricks) {
                if (brick != other && brick.isDirectlyAbove(other)) {
                    brick.supporters.add(other);
                    other.supporting.add(brick);
                }
            }
        }
    }

    private static int countFallenBricks(List<Brick> bricks) {
        var totalFallenBricks = 0;
        Set<Brick> fallenBricks = new HashSet<>();

        for (var brick : bricks) {
            if (!brick.safeToRemove) {
                fallenBricks.add(brick);
                cascadeFalling(brick, fallenBricks);
                totalFallenBricks += fallenBricks.size() - 1;
                fallenBricks.clear();
            }
        }

        return totalFallenBricks;
    }

    private static void cascadeFalling(Brick brick, Set<Brick> fallenBricks) {
        for (var supporting : brick.supporting) {
            if (fallenBricks.containsAll(supporting.supporters)) {
                fallenBricks.add(supporting);
                cascadeFalling(supporting, fallenBricks);
            }
        }
    }

    @RequiredArgsConstructor
    @ToString
    static class Brick {
        char id;
        int startX, startY, startZ;
        int endX, endY, endZ;
        @ToString.Exclude
        Set<Brick> supporters = new HashSet<>();
        @ToString.Exclude
        Set<Brick> supporting = new HashSet<>();
        boolean safeToRemove = true;

        Brick(char id, String brickPosition) {
            this.id = id; // Easier to debug
            var parts = brickPosition.split("~");
            var starts = Util.parseLine(parts[0], ",", Integer::parseInt);
            var ends = Util.parseLine(parts[1], ",", Integer::parseInt);
            this.startX = starts.get(0);
            this.startY = starts.get(1);
            this.startZ = starts.get(2);
            this.endX = ends.get(0);
            this.endY = ends.get(1);
            this.endZ = ends.get(2);
        }

        boolean isDirectlyAbove(Brick other) {
            var overlapsX = this.startX <= other.endX && this.endX >= other.startX;
            var overlapsY = this.startY <= other.endY && this.endY >= other.startY;
            var isJustAbove = this.startZ == other.endZ + 1;
            return overlapsX && overlapsY && isJustAbove;
        }
    }

    @Data
    @AllArgsConstructor
    static class Point {
        int x, y;
    }

}
