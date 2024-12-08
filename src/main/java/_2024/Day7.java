package _2024;

import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.math.BigInteger;
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
}
