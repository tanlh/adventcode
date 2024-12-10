package _2024;

import lombok.AllArgsConstructor;
import util.Util;

import java.util.*;
import java.util.stream.IntStream;

public class Day9 {

    @AllArgsConstructor
    static class Space {
        int index, size;
    }

    @AllArgsConstructor
    static class File {
        int index, size, id;
    }

    private static List<Integer> blocks;
    private static LinkedList<Space> spaceMap;
    private static Stack<File> fileMap;

    public static void main(String[] args) {
        var diskMap = Util.readFileToLines().getFirst();
        parseDiskMap(diskMap);

        var part1 = compactDisk1();
        System.out.println("Part1: " + part1);

        // Parse to reset :))
        parseDiskMap(diskMap);
        var part2 = compactDisk2();
        System.out.println("Part2: " + part2);
    }

    private static void parseDiskMap(String diskMap) {
        blocks = new ArrayList<>();
        spaceMap = new LinkedList<>();
        fileMap = new Stack<>();
        int[] fileId = new int[1], blockIndex = new int[1];

        IntStream.range(0, diskMap.length()).mapMulti((i, _) -> {
            var size = Character.getNumericValue(diskMap.charAt(i));
            var isFile = i % 2 == 0;
            blocks.addAll(Collections.nCopies(size, isFile ? fileId[0] : -1));
            if (isFile) {
                fileMap.push(new File(blockIndex[0], size, fileId[0]));
                fileId[0]++;
            } else {
                spaceMap.add(new Space(blockIndex[0], size));
            }
            blockIndex[0] += size;
        }).forEach(_ -> {});
    }

    private static long calCheckSum(List<Integer> blocks) {
        return IntStream.range(0, blocks.size())
            .filter(i -> blocks.get(i) != -1)
            .mapToLong(i -> (long) i * blocks.get(i))
            .sum();
    }

    private static long compactDisk1() {
        while (blocks.contains(-1)) {
            var lmSpace = spaceMap.poll();
            var moves = 0;

            for (var i = blocks.size() - 1; i > (lmSpace.index + lmSpace.size - 1) && moves < lmSpace.size; i--) {
                if (blocks.get(i) != -1) {
                    blocks.set(lmSpace.index + moves++, blocks.get(i));
                    blocks.set(i, -1);
                }
            }

            while (blocks.getLast() == -1) blocks.removeLast();
            spaceMap.removeIf(space -> space.index >= blocks.size());
        }

        return calCheckSum(blocks);
    }

    private static long compactDisk2() {
        while (true) {
            if (fileMap.isEmpty()) break;

            var currentFile = fileMap.pop();
            var foundSpace = spaceMap.stream()
                .filter(space -> space.size >= currentFile.size)
                .findFirst().orElse(null);
            if (foundSpace == null || foundSpace.index > currentFile.index) continue;

            for (var i = 0; i < currentFile.size; i++) {
                blocks.set(foundSpace.index + i, currentFile.id);
                blocks.set(currentFile.index + i, -1);
            }

            if (foundSpace.size > currentFile.size) {
                foundSpace.size = foundSpace.size - currentFile.size;
                foundSpace.index = foundSpace.index + currentFile.size;
            } else {
                spaceMap.remove(foundSpace);
            }
        }

        return calCheckSum(blocks);
    }

}
