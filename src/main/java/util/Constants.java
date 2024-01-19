package util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;

public interface Constants {

    /**
     * L, R, U, D
     */
    int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    BiMap<Character, Integer> DIRMAP = HashBiMap.create(Map.of('<', 0, '>', 1, '^', 2, 'v', 3));

}
