import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Test {

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Range {
        private long min;
        private long max;
        @EqualsAndHashCode.Exclude
        private long desSrcDiff;

        public Range(long min, long max) {
            new Range(min, max, 0);
        }

        public Range getOverlap(Range other) {
            long overlapMin = Math.max(this.min, other.min);
            long overlapMax = Math.min(this.max, other.max);

            if (overlapMin <= overlapMax) {
                return new Range(overlapMin, overlapMax, other.desSrcDiff);
            } else {
                return null;
            }
        }

        public Range adjustDesSrcDiff() {
            return new Range(min + desSrcDiff, max + desSrcDiff, 0);
        }
    }

    public static void main(String[] args) {
        findMin();
    }

    private static void findMin() {
        var mappingGroups = readFile("file.txt");
        List<Range> prevRanges = getSeedRanges(mappingGroups);
        System.err.println("Seeds: " + prevRanges);
        System.err.println();

        List<Range> currRanges = new ArrayList<>();

        for (int gIndx = 1; gIndx < mappingGroups.size(); gIndx++) {
            var mappingGroup = mappingGroups.get(gIndx);
            System.err.println(mappingGroup.get(0));

            for (var prevRange : prevRanges) {
                for (int i = 1; i < mappingGroup.size(); i++) { // ignore first line of group which is a-to-b
                    var currMapping = lineToListLong(mappingGroup.get(i));
                    var desStart = currMapping.get(0);
                    var srcStart = currMapping.get(1);
                    var range = currMapping.get(2);
                    var mappingRange = new Range(srcStart, srcStart + range - 1, desStart - srcStart);

                    var overlapRange = prevRange.getOverlap(mappingRange);
                    if (overlapRange != null) {
                        currRanges.add(overlapRange);
                    }
                }
            }

            if (currRanges.isEmpty()) { // no overlapping
                currRanges.addAll(prevRanges);
            } else {
                // add the non-overlapping ranges
                var nonOverlappingRanges = findNonOverlappingRanges(new ArrayList<>(prevRanges), new ArrayList<>(currRanges));
                System.err.println("Non overlap: " + nonOverlappingRanges);

                currRanges.addAll(nonOverlappingRanges);
            }

            System.err.println("prev ranges: " + prevRanges);
            System.err.println("curr ranges: " + currRanges);
            System.err.println();

            prevRanges = currRanges.stream()
                .map(Range::adjustDesSrcDiff)
                .collect(Collectors.toList());
            currRanges.clear();
        }

        System.err.println("Final mapping: " + prevRanges);
        var min = prevRanges.stream()
            .map(Range::getMin)
            .min(Comparator.naturalOrder())
            .get();
        System.err.println("Min: " + min);
    }

    // From GPT
    private static List<Range> findNonOverlappingRanges(List<Range> firstList, List<Range> secondList) {
        List<Range> nonOverlappingRanges = new ArrayList<>();

        for (Range firstRange : firstList) {
            boolean isOverlapping = false;

            for (Range secondRange : secondList) {
                if (firstRange.min <= secondRange.max && secondRange.min <= firstRange.max) {
                    isOverlapping = true;

                    if (firstRange.min < secondRange.min) {
                        nonOverlappingRanges.add(new Range(firstRange.min, secondRange.min - 1));
                    }

                    if (firstRange.max > secondRange.max) {
                        nonOverlappingRanges.add(new Range(secondRange.max + 1, firstRange.max));
                    }

                    break;
                }
            }

            if (!isOverlapping) {
                nonOverlappingRanges.add(firstRange);
            }
        }

        return nonOverlappingRanges;
    }

    // From GPT
    public static List<List<String>> readFile(String filePath) {
        List<List<String>> result = new ArrayList<>();

        try (var reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<String> mappingGroups = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotEmpty(line)) {
                    mappingGroups.add(line);
                    continue;
                }

                result.add(new ArrayList<>(mappingGroups));
                mappingGroups.clear();
            }

            // Add the last set of lines if the file doesn't end with an empty line
            if (!mappingGroups.isEmpty()) {
                result.add(new ArrayList<>(mappingGroups));
            }
        } catch (IOException ignored) {
        }

        return result;
    }

    public static List<Long> lineToListLong(String line) {
        return Arrays.stream(line.split(" "))
            .filter(StringUtils::isNotBlank)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    private static List<Range> getSeedRanges(List<List<String>> mappingGroups) {
        var seeds = lineToListLong(mappingGroups.get(0).get(0).split(":")[1]);
        List<Range> seedRanges = new ArrayList<>();

        for (int i = 0; i < seeds.size() - 1; i += 2) {
            var startSeed = seeds.get(i);
            var range = seeds.get(i + 1);
            seedRanges.add(new Range(startSeed, startSeed + range - 1));
        }

        return seedRanges;
    }

}
