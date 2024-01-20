package _2022;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import util.Util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 {

    public static void main(String[] args) {
        var pairs = Util.readFileToBlocks();
        var correctSum = IntStream.rangeClosed(1, pairs.size())
            .filter(i -> compareList(JSON.parseArray(pairs.get(i - 1).get(0)), JSON.parseArray(pairs.get(i - 1).get(1))) <= 0)
            .sum();
        System.err.println("Part 1: " + correctSum);

        var decoder1 = new JSONArray(List.of(new JSONArray(List.of(2))));
        var decoder2 = new JSONArray(List.of(new JSONArray(List.of(6))));
        var packets = Util.readFileToLines().stream()
            .filter(StringUtils::isNotBlank)
            .map(JSON::parseArray)
            .collect(Collectors.toList());
        packets.add(decoder1);
        packets.add(decoder2);
        packets.sort(Day13::compareList);

        var product = IntStream.rangeClosed(1, packets.size())
            .filter(i -> packets.get(i - 1).equals(decoder1) || packets.get(i - 1).equals(decoder2))
            .reduce(1, (a, b) -> a * b);
        System.err.println("Part 2: " + product);
    }

    private static int compareList(JSONArray json1, JSONArray json2) {
        for (int i = 0, minLength = Math.min(json1.size(), json2.size()); i < minLength; i++) {
            var compare = compareElement(json1.get(i), json2.get(i));
            if (compare != 0) {
                return compare;
            }
        }

        return Integer.compare(json1.size(), json2.size());
    }

    private static int compareElement(Object o1, Object o2) {
        if (o1 instanceof Integer i1 && o2 instanceof Integer i2) {
            return Integer.compare(i1, i2);
        }
        if (o1 instanceof JSONArray a1 && o2 instanceof JSONArray a2) {
            return compareList(a1, a2);
        }
        return compareList(
            o1 instanceof Integer ? new JSONArray(List.of(o1)) : (JSONArray) o1,
            o2 instanceof Integer ? new JSONArray(List.of(o2)) : (JSONArray) o2
        );
    }

}