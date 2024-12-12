package _2023;

import util.LongPoint;
import util.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 {

    public static void main(String[] args) {
        var instructions = parseInstructions();
        var vertices = parseVertices(instructions);

        /**
         * This is Shoelace formula
         */
        var area = IntStream.range(0, vertices.size() - 1)
            .mapToLong(i -> {
                var p1 = vertices.get(i);
                var p2 = vertices.get(i + 1);
                return p1.x * p2.y - p2.x * p1.y;
            })
            .sum();
        area = Math.abs(area) / 2;

        var boundary = instructions.stream()
            .map(Instruction::getDistance)
            .reduce(0L, Long::sum);

        /**
         * Now, based on the shoelace formula, we have the area of polygon, but the point is in the middle
         * of coordinate square. We are missing the other half of each square.
         *
         * Here comes the Pick's theorem: A = i + b / 2 - 1
         * - A: area of polygon
         * - i: inside points
         * - b: boundary point, asy to calculate, it's the sum of all the step
         * => We can find completely inside points: i = A - b/2 + 1
         * => Finally, just add the boundary points
         * => result = i + b = A - b/2 + 1 + b = A + b/2 + 1
         */
        var result = area + (boundary / 2) + 1;
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

    private static List<LongPoint> parseVertices(List<Instruction> instructions) {
        int x = 0, y = 0;
        List<LongPoint> vertices = new ArrayList<>();

        for (var instr : instructions) {
            var dx = instr.direction == 'L' ? -1 : instr.direction == 'R' ? 1 : 0;
            var dy = instr.direction == 'U' ? -1 : instr.direction == 'D' ? 1 : 0;
            x += dx * instr.distance;
            y += dy * instr.distance;
            vertices.add(new LongPoint(x, y));
        }

        return vertices;
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
