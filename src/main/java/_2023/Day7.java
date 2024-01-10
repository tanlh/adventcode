package _2023;

import util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.entry;

public class Day7 {

    static Map<Character, Integer> cardValues = Map.ofEntries(
        entry('A', 14),
        entry('K', 13),
        entry('Q', 12),
        entry('T', 10),
        entry('9', 9),
        entry('8', 8),
        entry('7', 7),
        entry('6', 6),
        entry('5', 5),
        entry('4', 4),
        entry('3', 3),
        entry('2', 2),
        entry('J', 1)
    );

    public static void main(String[] args) {
        var sortedHands = Util.readFileToLines().stream()
            .map(line -> {
                var parts = line.split(" ");
                return new Hand(parts[0], Integer.parseInt(parts[1]));
            })
            .sorted()
            .collect(Collectors.toList());

        System.err.println("Hands sorted: " + sortedHands);

        var totalWinnings = IntStream.range(0, sortedHands.size())
            .map(i -> sortedHands.get(i).getBid() * (i + 1))
            .sum();
        System.out.println("Total winnings: " + totalWinnings);
    }

    @AllArgsConstructor
    @Getter
    @ToString
    static class Hand implements Comparable<Hand> {
        String hand;
        int bid;

        private int getRank() {
            // Case hand by default is 5 of kind
            if (hand.chars().distinct().count() == 1) {
                return 7;
            }

            var maxRank = 0;
            var remaining = hand.replace("J", "");
            for (int i = 0; i < remaining.length(); i++) {
                var mutated = hand.replace('J', remaining.charAt(i));
                var rank = calculateHandType(mutated);
                if (rank > maxRank) {
                    maxRank = rank;
                }
            }

            return maxRank;
        }

        private int calculateHandType(String hand) {
            if (hand.chars().distinct().count() == 1) { // 5 of kind
                return 7;
            }

            Map<Character, Integer> countMap = new HashMap<>();
            for (char card : hand.toCharArray()) {
                countMap.put(card, countMap.getOrDefault(card, 0) + 1);
            }

            if (countMap.containsValue(4)) {
                return 6; // 4 of kind
            }
            if (countMap.containsValue(3) && countMap.containsValue(2)) {
                return 5; // full house
            }
            if (countMap.containsValue(3) && countMap.size() == 3) {
                return 4; // 3 of kind
            }
            if (countMap.size() == 3 && countMap.containsValue(2)) {
                return 3; // 2 pair
            }
            if (countMap.size() == 4 && countMap.containsValue(2)) {
                return 2; // one pair
            }
            return 1; // high card
        }

        @Override
        public int compareTo(Hand other) {
            var currRank = this.getRank();
            var otherRank = other.getRank();
            if (currRank != otherRank) {
                return Integer.compare(currRank, otherRank);
            }

            for (int i = 0; i < hand.length(); i++) {
                var compareValue = cardValues.get(hand.charAt(i)).compareTo(cardValues.get(other.hand.charAt(i)));
                if (compareValue != 0) {
                    return compareValue;
                }
            }

            // Doesn't happen
            return 0;
        }
    }
}
