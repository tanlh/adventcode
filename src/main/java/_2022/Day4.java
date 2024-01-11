package _2022;

import util.Range;
import util.Util;

public class Day4 {

    public static void main(String[] args) {
        int overlap = 0, contain = 0;

        for (var line : Util.readFileToLines()) {
            var parts = line.split(",");
            var section1 = Util.parseLine(parts[0], "-", Integer::parseInt);
            var section2 = Util.parseLine(parts[1], "-", Integer::parseInt);
            var range1 = new Range(section1.get(0), section1.get(1));
            var range2 = new Range(section2.get(0), section2.get(1));
            var intersect = range1.intersect(range2);

            if (intersect != null) {
                overlap++;
                if (intersect.equals(range1) || intersect.equals(range2)) {
                    contain++;
                }
            }
        }

        System.err.println("Part 1: " + contain);
        System.err.println("Part 2: " + overlap);
    }

}
