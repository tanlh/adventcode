package _2022;

import util.Util;

public class Day10 {

    public static void main(String[] args) {
        var screen = new String[]{"", "", "", "", "", ""};

        int x = 1, cycle = 0, sum = 0;
        for (var signal : Util.readFileToLines()) {
            for (int i = 0; i < ("noop".equals(signal) ? 1 : 2); i++) {
                cycle++;
                var line = (cycle - 1) / 40;
                var crt = (cycle - 1) % 40;
                screen[line] += Math.abs(x - crt) <= 1 ? "#" : ".";
                sum += (cycle - 20 % 40 == 0 ? cycle * x : 0);
            }
            if (!"noop".equals(signal)) {
                x += Integer.parseInt(signal.substring(5));
            }
        }

        System.err.println("Sum: " + sum);
        System.err.println(String.join("\n", screen));
    }

}