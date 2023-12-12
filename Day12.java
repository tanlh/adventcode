import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Test {

    private static final int COPIES = 5; // Number of times to repeat the pattern and groups
    private static final Map<String, Long> memo = new HashMap<>();

    public static void main(String[] args) throws Exception {
        var totalArrangements = Files.readAllLines(Paths.get("file.txt"))
            .stream()
            .map(Test::countArrangements)
            .reduce(0L, Long::sum);
        System.err.println("Total number of arrangements: " + totalArrangements);
    }

    private static long countArrangements(String line) {
        var parts = line.split(" ");
        var pattern = String.join("?", Collections.nCopies(COPIES, parts[0]));
        int[] groups = Arrays.stream(String.join(",", Collections.nCopies(COPIES, parts[1])).split(","))
            .mapToInt(Integer::parseInt)
            .toArray();

        memo.clear();
        return countArrangements(pattern, groups, 0, 0, 0, false);
    }

    private static long countArrangements(String pattern, int[] groups, int index, int groupIndex, int damagedCount, boolean prevDamaged) {
        // Check if the result is already computed.
        String memoKey = getMemoKey(index, groupIndex, damagedCount, prevDamaged);
        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        // If all groups are done, we just need to fill the remaining with operational springs
        if (groupIndex == groups.length) {
            for (int i = index; i < pattern.length(); i++) {
                if (pattern.charAt(i) == '#') {
                    return 0;
                }
            }
            return 1;
        }

        // If all characters are processed, check if we have a valid configuration
        if (index == pattern.length()) {
            // All groups should be accounted for and the last group should be completed
            return (groupIndex == groups.length - 1 && damagedCount == groups[groupIndex]) ? 1 : 0;
        }

        char current = pattern.charAt(index);
        long count = 0;

        // When the current character is unknown or damaged, and we have not exceeded the group size
        if ((current == '?' || current == '#') && damagedCount < groups[groupIndex]) {
            count += countArrangements(pattern, groups, index + 1, groupIndex, damagedCount + 1, true);
        }

        // When the current character is unknown or operational, we may need to move to the next group
        if (current == '?' || current == '.') {
            // If the previous was damaged and we completed a group, or if the previous was not damaged
            if (!prevDamaged || damagedCount == groups[groupIndex]) {
                count += countArrangements(pattern, groups, index + 1, prevDamaged && damagedCount == groups[groupIndex] ? groupIndex + 1 : groupIndex, 0, false);
            }
        }

        // Save the computed result.
        memo.put(memoKey, count);
        return count;
    }

    private static String getMemoKey(int index, int groupIndex, int damagedCount, boolean prevDamaged) {
        return index + "-" + groupIndex + "-" + damagedCount + "-" + prevDamaged;
    }

}
