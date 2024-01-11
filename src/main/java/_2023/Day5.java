package _2023;

import util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

    public static void main(String[] args) {
        var blocks = Util.readFileToBlocks();
        var prevRanges = buildSeedRanges(blocks);

        List<Range> currRanges = new ArrayList<>();
        for (int gIndx = 1; gIndx < blocks.size(); gIndx++) {
            var block = blocks.get(gIndx);

            for (var prevRange : prevRanges) {
                // ignore first line of group which is a-to-b
                for (int i = 1; i < block.size(); i++) {
                    var currMapping = Util.parseLine(block.get(i), " ", Long::parseLong);
                    var desStart = currMapping.get(0);
                    var srcStart = currMapping.get(1);
                    var range = currMapping.get(2);
                    var mappingRange = new Range(srcStart, srcStart + range - 1, desStart - srcStart);

                    var overlapRange = prevRange.intersect(mappingRange);
                    if (overlapRange != null) {
                        currRanges.add(overlapRange);
                    }
                }
            }

            // no overlapping
            if (currRanges.isEmpty()) {
                currRanges.addAll(prevRanges);
            } else {
                // add the non-overlapping ranges
                currRanges.addAll(findNonOverlappingRanges(new ArrayList<>(prevRanges), new ArrayList<>(currRanges)));
            }

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

    private static List<Range> buildSeedRanges(List<List<String>> mappingGroups) {
        var seeds = Util.parseLine(mappingGroups.get(0).get(0).split(":")[1], " ", Long::parseLong);
        List<Range> seedRanges = new ArrayList<>();

        for (int i = 0; i < seeds.size() - 1; i += 2) {
            var startSeed = seeds.get(i);
            var range = seeds.get(i + 1);
            seedRanges.add(new Range(startSeed, startSeed + range - 1));
        }

        return seedRanges;
    }

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

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    static class Range {
        long min;
        long max;
        @EqualsAndHashCode.Exclude
        long desSrcDiff;

        public Range(long min, long max) {
            new Range(min, max, 0);
        }

        public Range intersect(Range other) {
            var overlapMin = Math.max(this.min, other.min);
            var overlapMax = Math.min(this.max, other.max);
            return overlapMin <= overlapMax ? new Range(overlapMin, overlapMax, other.desSrcDiff) : null;
        }

        public Range adjustDesSrcDiff() {
            return new Range(min + desSrcDiff, max + desSrcDiff, 0);
        }
    }

}
