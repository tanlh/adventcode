package util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;

public interface Constants {

    /**
     * Note: pair is col, row direction. In some challenge I use row, col direction. It depends which one is easier
     */
    int[] LEFT = {-1, 0};
    int[] RIGHT = {1, 0};
    int[] TOP = {0, -1};
    int[] BOTTOM = {0, 1};
    int[][] DIRECTIONS = {LEFT, RIGHT, TOP, BOTTOM};
    int[] TOP_LEFT = {-1, -1};
    int[] TOP_RIGHT = {1, -1};
    int[] BOTTOM_LEFT = {-1, 1};
    int[] BOTTOM_RIGHT = {1, 1};
    int[][] FULL_DIRECTIONS = {LEFT, RIGHT, TOP, BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};

    BiMap<Character, Integer> DIRMAP = HashBiMap.create(Map.of('<', 0, '>', 1, '^', 2, 'v', 3));

}
