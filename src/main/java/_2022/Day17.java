package _2022;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import util.LongPoint;
import util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    static int CAVE_WIDTH = 7;
    static List<LongPoint> restRocks = new ArrayList<>();
    static String jetPattern;
    static Map<RockState, Pair<Long, Long>> rockStateMap = new HashMap<>();

    @Data
    @AllArgsConstructor
    static class RockState {
        List<Long> xs;
        int rockShape;
        int jetIndex;
    }

    public static void main(String[] args) {
        new Day17().simulate();
    }

    private void simulate() {
        jetPattern = Util.readFileToLines().get(0);

        var part1Height = simulateRockFall(0L, 0L, 2022, -1);
        System.err.println("Part 1: " + part1Height);

        var part2Height = simulateRockFall(0, 0, 1_000_000_000_000L, -1);
        System.err.println(part2Height);
    }

    private long simulateRockFall(long currentHeight, long startRock, long endRock, int jetIndex) {
        var totalHeight = currentHeight;

        for (var rockCount = startRock; rockCount < endRock; rockCount++) {
            var rock = generateRock(rockCount, totalHeight + 4);

            while (true) {
                jetIndex = (jetIndex + 1) % jetPattern.length();
                if (jetPattern.charAt(jetIndex) == '>') {
                    if (canMoveRight(rock)) {
                        moveRight(rock);
                    }
                } else if (jetPattern.charAt(jetIndex) == '<') {
                    if (canMoveLeft(rock)) {
                        moveLeft(rock);
                    }
                }

                if (isRest(rock)) {
                    restRocks.addAll(rock);
                    totalHeight = Math.max(totalHeight, rock.stream().mapToLong(p -> p.y).max().getAsLong());

                    var rockState = new RockState(rock.stream().map(p -> p.x).collect(Collectors.toList()), (int) rockCount % 5, jetIndex);
                    if (rockStateMap.containsKey(rockState)) {
                        System.err.println(rockState);
                        System.err.println("Rock & height: " + rockCount + ", " + totalHeight);
                        System.err.println("=======");
                    } else {
                        rockStateMap.put(rockState, Pair.of(rockCount, totalHeight));
                    }

                    break;
                }
                rock.forEach(p -> p.y--);
            }
        }

        return totalHeight;
    }

    private List<LongPoint> generateRock(long rockIndex, long y) {
        return switch ((int) (rockIndex % 5)) {
            // -
            case 0 -> List.of(new LongPoint(3, y), new LongPoint(4, y), new LongPoint(5, y), new LongPoint(6, y));
            // +
            case 1 ->
                List.of(new LongPoint(3, y + 1), new LongPoint(4, y + 1), new LongPoint(5, y + 1), new LongPoint(4, y), new LongPoint(4, y + 2));
            // _|
            case 2 ->
                List.of(new LongPoint(3, y), new LongPoint(4, y), new LongPoint(5, y), new LongPoint(5, y + 1), new LongPoint(5, y + 2));
            // |
            case 3 -> List.of(new LongPoint(3, y), new LongPoint(3, y + 1), new LongPoint(3, y + 2), new LongPoint(3, y + 3));
            // square
            default -> List.of(new LongPoint(3, y), new LongPoint(3, y + 1), new LongPoint(4, y), new LongPoint(4, y + 1));
        };
    }

    private boolean canMoveRight(List<LongPoint> rockShape) {
        return rockShape.stream().noneMatch(p -> p.x == CAVE_WIDTH || restRocks.contains(new LongPoint(p.x + 1, p.y)));
    }

    private void moveRight(List<LongPoint> rockShape) {
        rockShape.forEach(p -> p.x++);
    }

    private boolean canMoveLeft(List<LongPoint> rockShape) {
        return rockShape.stream().noneMatch(p -> p.x == 1 || restRocks.contains(new LongPoint(p.x - 1, p.y)));
    }

    private void moveLeft(List<LongPoint> rockShape) {
        rockShape.forEach(p -> p.x--);
    }

    private boolean isRest(List<LongPoint> rockShape) {
        return rockShape.stream().anyMatch(p -> p.y == 1 || restRocks.contains(new LongPoint(p.x, p.y - 1)));
    }
}
