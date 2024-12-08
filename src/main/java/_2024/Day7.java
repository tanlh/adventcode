package _2024;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Day7 {

    private static boolean checkEquation(BigInteger[] nums, BigInteger target) {
        Map<Integer, Map<BigInteger, Boolean>> memo = new HashMap<>();
        return checkEquationHelper(nums, target, 0, BigInteger.ZERO, memo);
    }

    private static boolean checkEquationHelper(BigInteger[] nums, BigInteger target, int index, BigInteger current, Map<Integer, Map<BigInteger, Boolean>> memo) {
        if (index == nums.length) {
            return current.equals(target);
        }

        if (memo.containsKey(index) && memo.get(index).containsKey(current)) {
            return memo.get(index).get(current);
        }

        boolean result = false;

        // Try addition
        if (checkEquationHelper(nums, target, index + 1, current.add(nums[index]), memo)) {
            result = true;
        } else if (index == 0 || checkEquationHelper(nums, target, index + 1, current.multiply(nums[index]), memo)) {
            // Try multiplication (but not if it's the first number and we haven't added anything yet)
            result = true;
        }

        memo.putIfAbsent(index, new HashMap<>());
        memo.get(index).put(current, result);

        return result;
    }
}
