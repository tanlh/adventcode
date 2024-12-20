package _2024;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class Day17 {

    record Pattern(String xorValue, String suffix, int... indices) {}

    private static final List<Integer> steps = List.of(2, 4, 1, 2, 7, 5, 1, 7, 4, 4, 0, 3, 5, 5, 3, 0);
    private static final List<String> solutions = new ArrayList<>();
    private static String currentResult = "0".repeat(7);

    // The program run these steps:
    // 1. b = a % 8 (gets last 3 bits of a)
    // 2. b = b ⊕ 2 (XOR with 2)
    // 3. c = a >> b (right shift a by b bits)
    // 4. b = b ⊕ 7
    // 5. b = b ⊕ c
    // 6. a = a >> 3 (divide by 8)
    // 7. output b % 8
    // 8. repeat if a ≠ 0
    // After testing, a have at least 46 bits. Test logics:
    // (x2)(x1)(x0)
    // case1 000 -> 010 -> 101 -> 101^(x4)(x3)0
    // case2 001 -> 011 -> 100 -> 100^(x5)(x4)(x3)
    // case3 010 -> 000 -> 111 -> 111^010 = 101 = 5
    // case4 011 -> 001 -> 110 -> 110^(x3)01
    // case5 100 -> 110 -> 001 -> 001^(x8)(x7)(x6)
    // case6 101 -> 111 -> 000 -> 000^(x9)(x8)(x7)
    // case7 110 -> 100 -> 011 -> 011^(x6)(x5)(x4)
    // case8 111 -> 101 -> 010 -> 010^(x7)(x6)(x5)
    // Apply reverse engineer
    private static final Map<String, Pattern> PATTERNS = new LinkedHashMap<>() {{
        put("000", new Pattern("101", "0", 1, 0));
        put("001", new Pattern("100", "", 2, 1, 0));
        put("010", new Pattern("", "", -5));
        put("011", new Pattern("110", "01", 0));
        put("100", new Pattern("001", "", 5, 4, 3));
        put("101", new Pattern("000", "", 6, 5, 4));
        put("110", new Pattern("011", "", 3, 2, 1));
        put("111", new Pattern("010", "", 4, 3, 2));
    }};

    public static void main(String[] args) {
        var output = runProgram(41644071L);
        System.err.println("Part 1: " + output);

        findSolution(steps.reversed(), 0);
        var part2 = Long.parseLong(solutions.getFirst(), 2);
        System.err.println("Part 2: " + part2);
        System.err.println("Test: " + runProgram(part2));
    }

    private static void findSolution(List<Integer> expected, int position) {
        if (position == expected.size()) {
            solutions.add(currentResult);
            return;
        }

        PATTERNS.keySet().stream()
            .filter(bits -> isValidCombination(bits, expected.get(position)))
            .forEach(bits -> {
                var prev = currentResult;
                currentResult += bits;
                findSolution(expected, position + 1);
                currentResult = prev;
            });
    }

    private static boolean isValidCombination(String bits, int output) {
        var p = PATTERNS.get(bits);
        return p.xorValue().isEmpty() ? output == -p.indices()[0]
            : (Integer.parseInt(p.xorValue(), 2) ^
            Integer.parseInt(Arrays.stream(p.indices())
                .mapToObj(i -> String.valueOf(currentResult.charAt(currentResult.length() - 1 - i)))
                .collect(joining()) + p.suffix(), 2)) == output;
    }

    private static String runProgram(long ia) {
        var outputs = new ArrayList<String>();
        var pointer = 0;

        long a = ia, b = 0, c = 0;

        while (pointer < steps.size()) {
//            System.err.printf("a: %d, b: %d, c: %d\n", a, b, c);
            var operand = steps.get(pointer + 1);
            var combo = switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> -1; // Ignored
            };

            switch (steps.get(pointer)) {
                case 0 -> a = (long) (a / Math.pow(2, combo));
                case 1 -> b ^= operand;
                case 2 -> b = combo;
                case 3 -> {
                    if (a == 0) break;
                    pointer = operand;
                    continue;
                }
                case 4 -> b ^= c;
                case 5 -> outputs.add(String.valueOf(combo % 8));
                case 6 -> b = (long) (a / Math.pow(2, combo));
                default -> c = (long) (a / Math.pow(2, combo));
            }

            pointer += 2;
        }

        return String.join(",", outputs);
    }

}
