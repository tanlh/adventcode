package _2024;

import util.Util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    record State(int index, BigInteger current) {
    }

    public static void main(String[] args) {
        var result = Util.readFileToLines().stream()
            .map(line -> {
                var parts = line.split(": ");
                var target = new BigInteger(parts[0]);
                var numbers = Util.parseLine(parts[1], "\\s+", BigInteger::new);
                Map<State, Boolean> memo = new HashMap<>();
                return isValidEq(target, numbers, numbers.getFirst(), 1, memo) ? target : BigInteger.ZERO;
            })
            .reduce(BigInteger.ZERO, BigInteger::add);
        System.err.println("Result: " + result);
    }

    private static boolean isValidEq(BigInteger target, List<BigInteger> numbers, BigInteger current, int index, Map<State, Boolean> memo) {
        if (index == numbers.size()) {
            return current.equals(target);
        }

        var state = new State(index, current);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        var next = numbers.get(index);
        var nextIndex = index + 1;
        var result = isValidEq(target, numbers, current.add(next), nextIndex, memo) ||
            isValidEq(target, numbers, current.multiply(next), nextIndex, memo) ||
            (index < numbers.size() && isValidEq(target, numbers, new BigInteger(current.toString() + next), nextIndex, memo));

        memo.put(state, result);
        return result;
    }

}
