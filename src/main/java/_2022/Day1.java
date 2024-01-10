package _2022;

import util.Util;

import java.util.Comparator;

public class Day1 {

    public static void main(String[] args) {
        var max = Util.readFileToBlocks().stream()
            .map(block -> block.stream().mapToInt(Integer::parseInt).sum())
//            .max(Comparator.naturalOrder()) // part 1
//            .get();
            .sorted(Comparator.reverseOrder()) // part 2
            .limit(3)
            .reduce(0, Integer::sum);
        System.err.println("Result: " + max);
    }

}
