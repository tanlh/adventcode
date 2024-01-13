package _2022;

import util.Util;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 {

    static Pattern movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    public static void main(String[] args) {
        var blocks = Util.readFileToBlocks();
        var stacks = parseStacks(blocks.get(0));

        for (var line : blocks.get(1)) {
            var matcher = movePattern.matcher(line);
            matcher.find();

            var moveSize = Integer.parseInt(matcher.group(1));
            var fromStack = stacks.get(Integer.parseInt(matcher.group(2)) - 1);
            var toStack = stacks.get(Integer.parseInt(matcher.group(3)) - 1);
            Deque<String> tempList = new LinkedList<>();
            for (int i = 0; i < moveSize; i++) {
                // part 1
//                toStack.offerFirst(fromStack.pollFirst());
                tempList.offerFirst(fromStack.pollFirst());
            }
            // part 2
            for (int i = 0; i < moveSize; i++) {
                toStack.offerFirst(tempList.pollFirst());
            }
        }

        var crates = stacks.stream().map(Deque::pollFirst).collect(Collectors.joining());
        System.err.println("Result: " + crates);
    }

    private static List<Deque<String>> parseStacks(List<String> stackConfig) {
        // Remove number line
        stackConfig.remove(stackConfig.size() - 1);

        var stackSize = (stackConfig.get(0).length() + 1) / 4;
        List<Deque<String>> stacks = new ArrayList<>(stackSize);
        for (int i = 0; i < stackSize; i++) {
            stacks.add(new LinkedList<>());
        }

        for (var line : stackConfig) {
            for (int i = 0; i < stackSize; i++) {
                var crate = line.substring(i * 4, Math.min(i * 4 + 4, line.length())).trim();
                if (!crate.isEmpty()) {
                    stacks.get(i).offerLast(crate.replaceAll("\\[|\\]", ""));
                }
            }
        }

        return stacks;
    }

}
