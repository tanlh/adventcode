package _2022;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import util.Util;

import java.util.Map;

public class Day2 {

    static Map<String, Integer> points = Map.of("X", 1, "Y", 2, "Z", 3);
    static Map<String, String> opponentPlays = Map.of("A", "X", "B", "Y", "C", "Z");
    static BiMap<String, String> winsAgainst = HashBiMap.create(Map.of("X", "Z", "Y", "X", "Z", "Y"));

    public static void main(String[] args) {
        int mark1 = 0, mark2 = 0;
        for (var round : Util.readFileToLines()) {
            var parts = round.split(" ");
            var opponent = opponentPlays.get(parts[0]);
            var outcome = parts[1];
            mark1 += score(opponent, outcome);
            mark2 += score(opponent, findPlayerItem(opponent, outcome));
        }

        System.err.println("Part 1: " + mark1);
        System.err.println("Part 2: " + mark2);
    }

    private static String findPlayerItem(String opponent, String outcome) {
        return switch (outcome) {
            case "X" -> winsAgainst.get(opponent);
            case "Y" -> opponent;
            default -> winsAgainst.inverse().get(opponent);
        };
    }

    private static int score(String opponent, String player) {
        return (player.equals(opponent) ? 3 : winsAgainst.get(player).equals(opponent) ? 6 : 0) + points.get(player);
    }

}