package _2022;

import lombok.AllArgsConstructor;
import util.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    @AllArgsConstructor
    static class File {
        long size;
        List<File> subFiles;

        static File newDir() {
            return new File(0, new ArrayList<>());
        }

        static File newFile(long size) {
            return new File(size, null);
        }

        long calculateSize() {
            if (size == 0 && !subFiles.isEmpty()) {
                size = subFiles.stream().mapToLong(File::calculateSize).sum();
            }
            return size;
        }
    }

    public static void main(String[] args) {
        Map<Path, File> dirMap = new HashMap<>();
        final var root = Path.of("/");
        var currentPath = root;
        dirMap.put(currentPath, File.newDir());

        for (var command : Util.readFileToLines()) {
            switch (command) {
                case "$ cd .." -> currentPath = currentPath.getParent();
                case "$ ls" -> {
                }
                default -> {
                    if (command.startsWith("$ cd ")) {
                        var newPath = currentPath.resolve(command.substring(5));
                        dirMap.computeIfAbsent(newPath, path -> File.newDir());
                        currentPath = newPath;
                    } else {
                        var parts = command.split(" ", 2);
                        var name = parts[1];
                        var file = parts[0].startsWith("dir") ?
                            dirMap.computeIfAbsent(currentPath.resolve(name), path -> File.newDir()) :
                            File.newFile(Long.parseLong(parts[0]));
                        dirMap.get(currentPath).subFiles.add(file);
                    }
                }
            }
        }

        var fileSizes = dirMap.values().stream().map(File::calculateSize).toList();

        var part1 = fileSizes.stream().filter(fileSize -> fileSize <= 100000).reduce(0L, Long::sum);
        System.err.println("Part 1: " + part1);

        var needDeleteSize = dirMap.get(root).size - 40_000_000L;
        var part2 = fileSizes.stream().filter(fileSize -> fileSize >= needDeleteSize).min(Long::compare).orElseThrow();
        System.err.println("Part 2: " + part2);
    }

}