package _2024;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day22 {

    private static final int STEPS = 2000;

    public static void main(String[] args) {
        var results = Util.readFileToLines().stream()
            .map(secret -> processSecret(Long.parseLong(secret)))
            .toList();

        var secretSum = results.stream().mapToLong(Pair::getKey).sum();
        System.err.println("Secret sum: " + secretSum);

        var bestSequence = results.stream()
            .flatMap(r -> r.getValue().entrySet().stream())
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow();
        System.err.printf("Best sequence: %s = %d", bestSequence.getKey(), bestSequence.getValue());
    }

    private static Pair<Long, Map<String, Integer>> processSecret(long secret) {
        var prices = new int[STEPS];
        var changes = new int[STEPS - 1];
        var priceMap = new HashMap<String, Integer>();

        for (var i = 0; i < STEPS; i++) {
            prices[i] = (int) (secret % 10);
            secret ^= (secret << 6) & 0xFFFFFF;
            secret ^= (secret >> 5) & 0xFFFFFF;
            secret ^= (secret << 11) & 0xFFFFFF;
            if (i > 0) changes[i - 1] = prices[i] - prices[i - 1];
            if (i >= 4) priceMap.putIfAbsent(StringUtils.join(changes[i - 4], changes[i - 3], changes[i - 2], changes[i - 1]), prices[i]);
        }
        return Pair.of(secret, priceMap);
    }

}
