package _2022;

import util.Util;

public class Day8 {

    public static void main(String[] args) {
        var grid = Util.readFileToGridInt();
        var rb = grid[0].length - 1;
        var db = grid.length - 1;

        var seen = 2 * rb + 2 * db;
        var maxView = 0;

        for (int i = 1; i < db; i++) {
            for (int j = 1; j < rb; j++) {
                var cur = grid[i][j];
                int l = j - 1, r = j + 1, t = i - 1, d = i + 1;

                while (l >= 0 && grid[i][l] < cur) l--;
                while (r <= rb && grid[i][r] < cur) r++;
                while (t >= 0 && grid[t][j] < cur) t--;
                while (d <= db && grid[d][j] < cur) d++;

                if (l < 0 || r > rb || t < 0 || d > db) seen++;

                var sceneScore = (j - Math.max(l, 0)) * (Math.min(r, rb) - j) * (i - Math.max(t, 0)) * (Math.min(d, db) - i);
                maxView = Math.max(maxView, sceneScore);
            }
        }

        System.err.println("Seen: " + seen);
        System.err.println("Max view: " + maxView);
    }

}