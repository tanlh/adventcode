package _2024;

import util.Util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    public static void main(String[] args) {
        var result = Util.readFileToLines().stream()
            .map(line -> {
                var parts = line.split(": ");
                var target = new BigInteger(parts[0]);
                var numbers = Util.parseLine(parts[1], "\\s+", BigInteger::new);
                Map<Integer, Map<BigInteger, Boolean>> memo = new HashMap<>();
                return isValidEq(target, numbers, numbers.getFirst(), 1, memo) ? target : BigInteger.ZERO;
            })
            .reduce(BigInteger.ZERO, BigInteger::add);
        System.err.println("Result: " + result);
    }

    private static boolean isValidEq(BigInteger target, List<BigInteger> numbers, BigInteger current, int index, Map<Integer, Map<BigInteger, Boolean>> memo) {
        if (index == numbers.size()) {
            return current.equals(target);
        }

        if (memo.getOrDefault(index, Map.of()).containsKey(current)) {
            return memo.get(index).get(current);
        }

        boolean result = false;

        if (isValidEq(target, numbers, current.add(numbers.get(index)), index + 1, memo)) {
            result = true;
        } else if (isValidEq(target, numbers, current.multiply(numbers.get(index)), index + 1, memo)) {
            result = true;
        } else if (index < numbers.size() && isValidEq(target, numbers, new BigInteger(current + numbers.get(index).toString()), index + 1, memo)) {
            result = true;
        }

        memo.putIfAbsent(index, new HashMap<>());
        memo.get(index).put(current, result);

        return result;
    }

}
