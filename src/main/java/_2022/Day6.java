package _2022;

import util.Util;

public class Day6 {

    static int packageLength = 14;

    public static void main(String[] args) {
        var line = Util.readFileToLines().get(0);

        for (int i = 0; i < line.length() - packageLength; i++) {
            var size = line.substring(i, i + packageLength).chars()
                .mapToObj(it -> (char) it)
                .distinct()
                .count();
            if (size == packageLength) {
                System.err.println("Result: " + (i + packageLength));
                System.exit(0);
            }
        }
    }

}
