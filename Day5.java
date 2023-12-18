package com.alibaba.logistics.station;

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
                for (int i = 1; i < block.size(); i++) { // ignore first line of group which is a-to-b
                    var currMapping = Util.parseLineToLongs(block.get(i), " ");
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
        var seeds = Util.parseLineToLongs(mappingGroups.get(0).get(0).split(":")[1], " ");
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
    public static class Range {
        long min;
        long max;
        @EqualsAndHashCode.Exclude
        long desSrcDiff;

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

}
