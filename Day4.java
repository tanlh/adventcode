import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day4 {

    public static void main(String[] args) {
        Map<Integer, Integer> resultMap = new HashMap<>();
        var gameIndex = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                calculateCard(line, resultMap, gameIndex);
                gameIndex++;
            }
        } catch (Exception ignored) {
        }

        var sum = resultMap.values().stream().reduce(0, Integer::sum);
        System.err.println("Sums: " + sum);
    }

    private static void calculateCard(String line, Map<Integer, Integer> result, int gameIndex) {
        var parts = line.split(":")[1].split("\\|");
        var resultCards = cardToValues(parts[0]);
        var myCards = cardToValues(parts[1]);
        var numberOfWinCards = CollectionUtils.intersection(resultCards, myCards).size();

        // Already scratch this card, so increase 1 scratch
        result.put(gameIndex, result.getOrDefault(gameIndex, 0) + 1);

        for (int k = gameIndex + 1; k <= gameIndex + numberOfWinCards; k++) {
            // Increase win cards
            result.put(k, result.getOrDefault(k, 0) + result.get(gameIndex));
        }
    }

    private static List<String> cardToValues(String card) {
        return Arrays.stream(card.split(" "))
            .map(String::trim)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }

}
