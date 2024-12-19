package _2024;

import util.Util;

import java.util.ArrayList;
import java.util.List;

public class Day17 {

    private static final String input = "2,4,1,2,7,5,1,7,4,4,0,3,5,5,3,0";
    private static final List<Integer> program = Util.parseLine(input, ",", Integer::parseInt);
    private static long a = 41644071L, b = 0, c = 0;

    public static void main(String[] args) {
        var output = runProgram();
        System.err.println("Part 1: " + output);

        // test code
        var counter = 0L;
        while (true) {
            a = counter;
            b = 0;
            c = 0;
            var out = runProgram();
            if (out.equals("2,4")) {
                break;
            }

            System.err.println("Running: " + counter);
            counter++;
        }
        System.err.println("Counter: " + counter + ", binary: " + Long.toBinaryString(counter));

        // "2,4" -> 1111
        // "2,4,1,2,7" -> 11110000001111
        // "2,4,1,2,7,5" -> 10011110000001111
        // "2,4,1,2,7,5,1" -> 100000011110000001111 check
        // "2,4,1,2,7,5,1,7" -> 11100000011110000001111
        // Notice pattern 0000001111
        // "2,4,1,2,7,5,1,7,4", guess a = 11100000011110000001111 -> decimal:
        a = Long.parseLong("111100000011100000011110000001111", 2);
        System.err.println(a);
        b = 0;
        c = 0;
        System.err.println(runProgram());
    }

    private static String runProgram() {
        var outputs = new ArrayList<String>();
        var pointer = 0;

        while (pointer < program.size()) {
//            System.err.printf("a: %d, b: %d, c: %d\n", a, b, c);
            var operand = program.get(pointer + 1);
            switch (program.get(pointer)) {
                case 0 -> a = (long) (a / Math.pow(2, getCombo(operand)));
                case 1 -> b = b ^ operand;
                case 2 -> b = getCombo(operand) % 8;
                case 3 -> {
                    if (a == 0) {
                        break;
                    }
                    pointer = operand;
                    continue;
                }
                case 4 -> b = b ^ c;
                case 5 -> outputs.add(String.valueOf(getCombo(operand) % 8));
                case 6 -> b = (long) (a / Math.pow(2, getCombo(operand)));
                default -> c = (long) (a / Math.pow(2, getCombo(operand)));
            }

            pointer += 2;
        }

        return String.join(",", outputs);
    }

    private static long getCombo(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalStateException("Unexpected value: " + operand);
        };
    }

}
