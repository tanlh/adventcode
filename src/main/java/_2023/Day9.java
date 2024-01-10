package _2023;

import util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) {
        var histories = Util.readFileToLines().stream()
            .map(line -> Util.parseLine(line, " ", Long::parseLong))
            .collect(Collectors.toList());

        var sumNext = histories.stream()
            .map(Day9::findNextValue)
            .reduce(0L, Long::sum);
        System.out.println("Sum next: " + sumNext);

        var sumBack = histories.stream()
            .peek(Collections::reverse)
            .map(Day9::findNextValue)
            .reduce(0L, Long::sum);
        System.out.println("Sum back: " + sumBack);
    }

    private static long findNextValue(List<Long> sequence) {
        var lastNumber = sequence.get(sequence.size() - 1);
        List<Long> lastNumbers = new ArrayList<>();

        while (anyNonZero(sequence)) {
            List<Long> nextSequence = new ArrayList<>();
            for (int i = 0; i < sequence.size() - 1; i++) {
                nextSequence.add(sequence.get(i + 1) - sequence.get(i));
            }
            lastNumbers.add(nextSequence.get(nextSequence.size() - 1));
            sequence = nextSequence;
        }

        return lastNumber + lastNumbers.stream().reduce(0L, Long::sum);
    }

    private static boolean anyNonZero(List<Long> sequence) {
        return sequence.stream().anyMatch(i -> i != 0);
    }

}
