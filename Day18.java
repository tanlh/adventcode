package com.alibaba.logistics.station;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 {

    public static void main(String[] args) {
        var instructions = parseInstructions();
        var vertices = parseVertices(instructions);
        var area = IntStream.range(0, vertices.size() - 1)
            .mapToLong(i -> {
                var p1 = vertices.get(i);
                var p2 = vertices.get(i + 1);
                return p1.x * p2.y - p2.x * p1.y;
            })
            .sum();
        var perimeter = instructions.stream()
            .map(Instruction::getDistance)
            .reduce(0L, Long::sum);

        /**
         * Pick's theorem
         */
        var result = Math.abs(area) / 2 + (perimeter / 2) + 1;
        System.err.println("Result: " + result);
    }

    private static List<Instruction> parseInstructions() {
        return Util.readFileToLines().stream()
            .map(line -> {
                var parts = line.split(" ");
                // part 1
//                var direction = parts[0].charAt(0);
//                var distance = Integer.parseInt(parts[1]);
                // part 2
                var direction = parts[2].charAt(7);
                var distance = Long.parseLong(parts[2].substring(2, 7), 16);
                return new Instruction(direction, distance);
            })
            .collect(Collectors.toList());
    }

    private static List<Point> parseVertices(List<Instruction> instructions) {
        int x = 0, y = 0;
        List<Point> vertices = new ArrayList<>();
        vertices.add(new Point(x, y));

        for (var instr : instructions) {
            var dx = instr.direction == 'L' ? -1 : instr.direction == 'R' ? 1 : 0;
            var dy = instr.direction == 'U' ? -1 : instr.direction == 'D' ? 1 : 0;
            x += dx * instr.distance;
            y += dy * instr.distance;
            vertices.add(new Point(x, y));
        }

        return vertices;
    }

    @Data
    @AllArgsConstructor
    static class Point {
        long x, y;
    }

    @Data
    static class Instruction {
        char direction;
        long distance;

        public Instruction(char direction, long distance) {
            switch (direction) {
                case '0': this.direction = 'R'; break;
                case '1': this.direction = 'D'; break;
                case '2': this.direction = 'L'; break;
                case '3': this.direction = 'U'; break;
                default: this.direction = direction;
            }
            this.distance = distance;
        }
    }

}
