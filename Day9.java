import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) {
        List<List<Integer>> histories = new ArrayList<>();
        try (var scanner = new Scanner(new File("file.txt"))) {
            while (scanner.hasNextLine()) {
                histories.add(lineToValues(scanner.nextLine()));
            }
        } catch (Exception ignored) {
        }

        int sumNext = histories.stream()
            .map(Test::findNextValue)
            .reduce(0, Integer::sum);
        System.out.println("Sum next: " + sumNext);

        int sumBack = histories.stream()
            .peek(Collections::reverse)
            .map(Test::findNextValue)
            .reduce(0, Integer::sum);
        System.out.println("Sum back: " + sumBack);
    }

    private static List<Integer> lineToValues(String line) {
        return Arrays.stream(line.split(" "))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    public static int findNextValue(List<Integer> sequence) {
        var lastNumber = sequence.get(sequence.size() - 1);
        List<Integer> lastNumbers = new ArrayList<>();

        while (anyNonZero(sequence)) {
            List<Integer> nextSequence = new ArrayList<>();
            for (int i = 0; i < sequence.size() - 1; i++) {
                nextSequence.add(sequence.get(i + 1) - sequence.get(i));
            }
            lastNumbers.add(nextSequence.get(nextSequence.size() - 1));
            sequence = nextSequence;
        }

        return lastNumber + lastNumbers.stream().reduce(0, Integer::sum);
    }

    private static boolean anyNonZero(List<Integer> sequence) {
        return sequence.stream().anyMatch(i -> i != 0);
    }

}
