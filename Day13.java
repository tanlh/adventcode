import lombok.SneakyThrows;
import org.apache.commons.collections4.ListUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 {

    public static void main(String[] args) {
        List<List<String>> blocks = readFileToBlocks();
        var sumLeft = 0;
        var sumAbove = 0;
        var smudgeLeft = 0;
        var smudgeAbove = 0;

        for (var block : blocks) {
            var left = findReflectIndex(block, 0);
            var above = findReflectIndex(transformList(block), 0);
            var sLeft = 0;
            var sAbove = 0;

            for (var sBlock : generateCombinations(block)) {
                sLeft = findReflectIndex(sBlock, left);
                sAbove = findReflectIndex(transformList(sBlock), above);
                if (sLeft > 0) {
                    break;
                }
                if (sAbove > 0) {
                    break;
                }
            }

            sumLeft += left;
            sumAbove += above;
            smudgeLeft += sLeft;
            smudgeAbove += sAbove;
        }

        System.err.println("left: " + sumLeft);
        System.err.println("above: " + sumAbove);
        System.err.println("part 1: " + ((sumAbove * 100) + sumLeft));
        System.err.println("part 2: " + ((smudgeAbove * 100) + smudgeLeft));
    }

    public static List<List<String>> generateCombinations(List<String> grid) {
        List<List<String>> allCombinations = new ArrayList<>();

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).length(); j++) {
                List<String> newGrid = new ArrayList<>(grid);

                char[] rowChars = newGrid.get(i).toCharArray();
                rowChars[j] = rowChars[j] == '#' ? '.' : '#';
                newGrid.set(i, new String(rowChars));

                allCombinations.add(newGrid);
            }
        }

        return allCombinations;
    }

    private static List<String> transformList(List<String> inputList) {
        List<String> resultList = new ArrayList<>();
        var length = inputList.get(0).length();
        for (int i = length - 1; i >= 0; i--) {
            StringBuilder sb = new StringBuilder();
            for (String str : inputList) {
                sb.append(str.charAt(i));
            }
            resultList.add(sb.toString());
        }

        return resultList;
    }

    public static int findReflectIndex(List<String> block, int originIndex) {
        var length = block.get(0).length();
        List<Integer> result = IntStream.range(1, length)
            .boxed()
            .collect(Collectors.toList());
        for (var line : block) {
            List<Integer> rowLine = new ArrayList<>();
            for (int j = 1; j < length; j++) {
                var l = j - 1;
                var r = j;
                var left = new StringBuilder();
                var right = new StringBuilder();
                while (l >= 0 && r < length) {
                    left.append(line.charAt(l));
                    right.append(line.charAt(r));
                    l -= 1;
                    r += 1;
                }
                if (left.toString().startsWith(right.toString()) || right.toString().startsWith(left.toString())) {
                    rowLine.add(j);
                }
            }
            result = ListUtils.intersection(result, rowLine);
        }

        return result.stream()
            .filter(i -> i != originIndex)
            .findFirst()
            .orElse(0);
    }

    @SneakyThrows
    private static List<List<String>> readFileToBlocks() {
        List<List<String>> blocks = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get("file.txt"));
        List<String> currentBlock = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty()) {
                if (!currentBlock.isEmpty()) {
                    blocks.add(new ArrayList<>(currentBlock));
                    currentBlock.clear();
                }
            } else {
                currentBlock.add(line);
            }
        }

        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock);
        }

        return blocks;
    }

}
