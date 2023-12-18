package com.alibaba.logistics.station;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class Day4 {

    public static void main(String[] args) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        var gameIndex = 1;

        for (var line : Util.readFileToLines()) {
            calculateCard(line, resultMap, gameIndex);
            gameIndex++;
        }

        var sum = resultMap.values().stream().reduce(0, Integer::sum);
        System.err.println("Sums: " + sum);
    }

    private static void calculateCard(String line, Map<Integer, Integer> result, int gameIndex) {
        var parts = line.split(":")[1].split("\\|");
        var resultCards = Util.splitLine(parts[0], " ");
        var myCards = Util.splitLine(parts[1], " ");
        var numberOfWinCards = CollectionUtils.intersection(resultCards, myCards).size();

        // Already scratch this card, so increase 1 scratch
        result.put(gameIndex, result.getOrDefault(gameIndex, 0) + 1);

        for (int k = gameIndex + 1; k <= gameIndex + numberOfWinCards; k++) {
            // Increase win cards
            result.put(k, result.getOrDefault(k, 0) + result.get(gameIndex));
        }
    }

}
