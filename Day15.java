package com.alibaba.logistics.station;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Day15 {

    public static void main(String[] args) {
        var steps = Util.readFileToLines().get(0).split(",");
        var sum1 = 0;
        Map<Integer, Map<String, Integer>> boxes = new LinkedHashMap<>();

        for (var step : steps) {
            sum1 += hash(step); // part 1
            processStep(step, boxes);
        }

        System.err.println("Sum part 1: " + sum1);

        AtomicInteger sum2 = new AtomicInteger();
        boxes.forEach((box, lenMap) -> {
            AtomicInteger i = new AtomicInteger(1);
            lenMap.forEach((label, len) -> {
                sum2.addAndGet((box + 1) * i.get() * len);
                i.getAndIncrement();
            });
        });

        System.err.println("Sum part 2: " + sum2);
    }

    private static void processStep(String step, Map<Integer, Map<String, Integer>> boxes) {
        var label = "";
        var lens = 0;
        var isEquals = step.contains("=");

        if (isEquals) {
            var parts = step.split("=");
            label = parts[0];
            lens = Integer.parseInt(parts[1]);
        } else {
            label = step.split("-")[0];
        }

        var boxNum = hash(label);
        if (isEquals) {
            boxes.computeIfAbsent(boxNum, k -> new LinkedHashMap<>())
                .put(label, lens);
        } else {
            boxes.getOrDefault(boxNum, new LinkedHashMap<>()).remove(label);
        }
    }

    private static int hash(String step) {
        var hash = 0;
        for (int i = 0; i < step.length(); i++) {
            hash += step.charAt(i);
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

}
