package _2022;

import lombok.AllArgsConstructor;
import util.Range;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15 {

    static Pattern sensorPattern = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
    static long targetRow = 2_000_000;
    static long area = 4_000_000;

    @AllArgsConstructor
    static class Sensor {
        long x, y, beaconX, beaconY;

        Sensor(String sensor) {
            var matcher = sensorPattern.matcher(sensor);
            matcher.find();
            this.x = Long.parseLong(matcher.group(1));
            this.y = Long.parseLong(matcher.group(2));
            this.beaconX = Long.parseLong(matcher.group(3));
            this.beaconY = Long.parseLong(matcher.group(4));
        }

        long manhattanDistance() {
            return Math.abs(x - beaconX) + Math.abs(y - beaconY);
        }
    }

    public static void main(String[] args) {
        var sensors = Util.readFileToLines().stream().map(Sensor::new).toList();

        var targetRowBeacons = sensors.stream()
            .filter(s -> s.beaconY == targetRow)
            .map(s -> s.beaconX)
            .collect(Collectors.toSet());
        var count = getExclusionRanges(sensors, targetRow).stream()
            .mapToLong(range -> range.length() - targetRowBeacons.stream().filter(b -> range.min <= b && b <= range.max).count())
            .sum();
        System.err.println("Part 1: " + count);

        /**
         * This loop will scan the exclusion ranges (x) on every horizontal line (y)
         * From the manual run, I see there are horizontal lines has 2 exclusion ranges that have exactly 1 missing point between them
         * The missing point is the distress beacon we are finding
         */
        for (int i = 0; i <= area; i++) {
            var ranges = getExclusionRanges(sensors, i);
            if (ranges.size() > 1) {
                System.err.println(ranges);
                System.err.println("Part 2: " + ((ranges.get(0).max + 1) * area + i));
                break;
            }
        }
    }

    private static List<Range> getExclusionRanges(List<Sensor> sensors, long targetRow) {
        List<Range> ranges = new ArrayList<>();
        for (var sensor : sensors) {
            var distance = sensor.manhattanDistance();
            if (Math.abs(sensor.y - targetRow) <= distance) {
                var range = distance - Math.abs(sensor.y - targetRow);
                ranges.add(new Range(sensor.x - range, sensor.x + range));
            }
        }
        return Util.mergeRanges(ranges);
    }

}