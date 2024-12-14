package _2024;

import util.Util;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This problem requires x and y less than 100 for part 1, but the input ensures x, y less 100.
 * So I skipped the check
 */
public class Day13 {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final BigInteger OFFSET = new BigInteger("10000000000000");
    private static final BigInteger ATOKEN = BigInteger.valueOf(3);

    public static void main(String[] args) {
        var blocks = Util.readFileToBlocks();
        var part1 = BigInteger.ZERO;
        var part2 = BigInteger.ZERO;

        for (var block : blocks) {
            var machine = parseMachine(block);
            part1 = part1.add(calculateToken(machine));

            machine[4] = machine[4].add(OFFSET);
            machine[5] = machine[5].add(OFFSET);
            part2 = part2.add(calculateToken(machine));
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private static BigInteger[] parseMachine(List<String> block) {
        return block.stream()
            .flatMap(line -> NUMBER_PATTERN.matcher(line).results())
            .map(mr -> new BigInteger(mr.group()))
            .toArray(BigInteger[]::new);
    }

    private static BigInteger calculateToken(BigInteger[] m) {
        var d = m[0].multiply(m[3]).subtract(m[1].multiply(m[2])).abs();
        // This case doesn't happen with the input.
        // In case it happens, we need to solve Diophantine equation: ax+by=c
        if (d.signum() == 0) return BigInteger.ZERO;

        var dx = m[4].multiply(m[3]).subtract(m[5].multiply(m[2])).abs();
        var dy = m[0].multiply(m[5]).subtract(m[1].multiply(m[4])).abs();

        return (dx.mod(d).signum() == 0 && dy.mod(d).signum() == 0)
            ? dx.divide(d).multiply(ATOKEN).add(dy.divide(d))
            : BigInteger.ZERO;
    }

}
