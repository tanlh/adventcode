package _2024;

import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    public static void main(String[] args) {
        var equations = Util.readFileToLines().stream()
            .map(line -> {
                var parts = line.split(": ");
                return Pair.of(new BigInteger(parts[0]), Util.parseLine(parts[1], "\\s+", BigInteger::new));
            })
            .toList();

        var part1 = equations.stream()
            .filter(equation -> {
                var isValid = isValid(equation);
                System.err.println("Equation: " + equation + " is valid: " + isValid);
                return isValid;
            })
            .map(Pair::getLeft)
            .reduce(BigInteger.ZERO, BigInteger::add);
        System.err.println("Part 1: " + part1);
    }

    private static boolean isValid(Pair<BigInteger, List<BigInteger>> equation) {
        Map<Integer, Map<BigInteger, Boolean>> memo = new HashMap<>();
        return isValidRecursive(equation, equation.getRight().getFirst(), 1, memo);
    }

    private static boolean isValidRecursive(Pair<BigInteger, List<BigInteger>> equation, BigInteger current, int index, Map<Integer, Map<BigInteger, Boolean>> memo) {
        if (index == equation.getRight().size()) {
            return current.equals(equation.getLeft());
        }

        if (memo.containsKey(index) && memo.get(index).containsKey(current)) {
            return memo.get(index).get(current);
        }

        boolean result = false;

        if (isValidRecursive(equation, current.add(equation.getRight().get(index)), index + 1, memo)) {
            result = true;
        } else if (isValidRecursive(equation, current.multiply(equation.getRight().get(index)), index + 1, memo)) {
            result = true;
        }

        memo.putIfAbsent(index, new HashMap<>());
        memo.get(index).put(current, result);

        return result;
    }

    private static List<List<BigInteger>> generateCombinations(List<BigInteger> numbers) {
        List<List<BigInteger>> result = new ArrayList<>();
        result.add(new ArrayList<>(numbers)); // Add original list

        for (int concatenations = 1; concatenations < numbers.size(); concatenations++) {
            generateCombinationsHelper(numbers, new ArrayList<>(), 0, concatenations, result);
        }

        return result;
    }

    private static void generateCombinationsHelper(List<BigInteger> numbers, List<BigInteger> current, int start, int concatenationsLeft, List<List<BigInteger>> result) {
        if (concatenationsLeft == 0) {
            current.addAll(numbers.subList(start, numbers.size()));
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < numbers.size() - 1; i++) {
            List<BigInteger> newCurrent = new ArrayList<>(current);
            BigInteger concatenated = new BigInteger(numbers.get(i).toString() + numbers.get(i + 1).toString());
            newCurrent.add(concatenated);
            newCurrent.addAll(numbers.subList(start, i));
            generateCombinationsHelper(numbers, newCurrent, i + 2, concatenationsLeft - 1, result);
        }
    }
}
