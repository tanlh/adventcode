import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day12 {

    public static void main(String[] args) throws Exception {
        long totalArrangements = 0;

        try (var scanner = new Scanner(new FileReader("file.txt"))) {
            while (scanner.hasNext()) {
                totalArrangements += countArrangements(scanner.nextLine());
            }
        }

        System.err.println("Total number of arrangements: " + totalArrangements);
    }

    private static long countArrangements(String line) {
        String[] parts = line.split(" ");
        char[] pattern = parts[0].toCharArray();
        int[] groups = Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).toArray();

        // List to hold all the possible patterns
        List<String> possiblePatterns = new ArrayList<>();
        generatePatterns(possiblePatterns, pattern, 0);

        // Count only the patterns that match the group sizes
        return possiblePatterns.stream().filter(p -> isValidPattern(p, groups)).count();
    }

    private static void generatePatterns(List<String> possiblePatterns, char[] pattern, int index) {
        if (index == pattern.length) {
            // Reached the end of the pattern, add it to the list
            possiblePatterns.add(new String(pattern));
            return;
        }

        if (pattern[index] == '?') {
            // Try with a damaged spring
            pattern[index] = '#';
            generatePatterns(possiblePatterns, pattern, index + 1);

            // Try with an operational spring
            pattern[index] = '.';
            generatePatterns(possiblePatterns, pattern, index + 1);

            // Reset to unknown for backtracking
            pattern[index] = '?';
        } else {
            // If not unknown, just continue to the next character
            generatePatterns(possiblePatterns, pattern, index + 1);
        }
    }

    private static boolean isValidPattern(String pattern, int[] groups) {
        int groupIndex = 0;
        int count = 0;

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '#') {
                count++;
            } else {
                if (count > 0) {
                    if (groupIndex >= groups.length || groups[groupIndex] != count) {
                        return false;
                    }
                    groupIndex++;
                }
                count = 0;
            }
        }

        if (count > 0) {
            if (groupIndex >= groups.length || groups[groupIndex] != count) {
                return false;
            }
            groupIndex++;
        }

        return groupIndex == groups.length;
    }
    
}
