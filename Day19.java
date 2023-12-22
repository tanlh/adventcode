package com.alibaba.logistics.station;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {

    static final int MIN_RANGE = 1;
    static final int MAX_RANGE = 4000;

    public static void main(String[] args) {
        var blocks = Util.readFileToBlocks();
        var workflows = parseWorkflows(blocks.get(0));
        var parts = parseParts(blocks.get(1));

        var sumTotal = 0L;
        for (var part : parts) {
            var result = processPart(part, workflows, "in");
            if ("A".equals(result)) {
                sumTotal += part.sumAttributes();
            }
        }

        System.out.println("Part 1: " + sumTotal);

        // Add A for stop condition of dfs
        workflows.put("A", new Workflow("A", null));

        List<List<Range>> acceptedPathRanges = new ArrayList<>();
        var fullRange = new Range(MIN_RANGE, MAX_RANGE);

        dfs(workflows, "in", fullRange, fullRange, fullRange, fullRange, acceptedPathRanges);

        AtomicLong part2 = new AtomicLong(0L);
        acceptedPathRanges.forEach(ranges ->
            part2.addAndGet(ranges.stream().map(Range::count).reduce(1L, (c1, c2) -> c1 * c2))
        );

        System.err.println(acceptedPathRanges);
        System.err.println("Part 2: " + part2);
    }

    private static void dfs(Map<String, Workflow> workflows, String name,
                            Range xRange, Range mRange, Range aRange, Range sRange,
                            List<List<Range>> acceptedCombinations) {
        var current = workflows.get(name);
        if (current == null) return;

        if ("A".equals(current.name)) {
            acceptedCombinations.add(List.of(xRange, mRange, aRange, sRange));
            return;
        }

        for (var rule : current.rules) {
            if (rule.condition == null) {
                dfs(workflows, rule.destination, xRange, mRange, aRange, sRange, acceptedCombinations);
                continue;
            }

            var newXRange = xRange;
            if (rule.xCondition != null) {
                newXRange = xRange.intersect(rule.xCondition);
                xRange = xRange.exclude(rule.xCondition);
            }

            var newMRange = mRange;
            if (rule.mCondition != null) {
                newMRange = mRange.intersect(rule.mCondition);
                mRange = mRange.exclude(rule.mCondition);
            }

            var newARange = aRange;
            if (rule.aCondition != null) {
                newARange = aRange.intersect(rule.aCondition);
                aRange = aRange.exclude(rule.aCondition);
            }

            var newSRange = sRange;
            if (rule.sCondition != null) {
                newSRange = sRange.intersect(rule.sCondition);
                sRange = sRange.exclude(rule.sCondition);
            }

            dfs(workflows, rule.destination, newXRange, newMRange, newARange, newSRange, acceptedCombinations);
        }
    }

    private static Map<String, Workflow> parseWorkflows(List<String> lines) {
        var workflowPattern = Pattern.compile("([a-z]+)\\{(.+)\\}");
        Map<String, Workflow> workflows = new HashMap<>();

        lines.forEach(line -> {
            var matcher = workflowPattern.matcher(line);
            matcher.matches();
            var name = matcher.group(1);
            var rules = Arrays.stream(matcher.group(2).split(","))
                .map(Day19::parseRule)
                .collect(Collectors.toList());
            workflows.put(name, new Workflow(name, rules));
        });

        return workflows;
    }

    private static Rule parseRule(String ruleStr) {
        if (!ruleStr.contains(":")) {
            return new Rule(ruleStr);
        }

        var parts = ruleStr.split(":");
        return new Rule(parts[0], parts[1]);
    }

    private static List<Part> parseParts(List<String> lines) {
        var partPattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}");

        return lines.stream()
            .map(line -> {
                var matcher = partPattern.matcher(line);
                matcher.matches();
                return new Part(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))
                );
            })
            .collect(Collectors.toList());
    }

    private static String processPart(Part part, Map<String, Workflow> workflows, String workflowName) {
        for (var rule : workflows.get(workflowName).rules) {
            if (rule.condition == null || rule.condition.test(part)) {
                if (rule.destination.equals("A") || rule.destination.equals("R")) {
                    return rule.destination;
                } else {
                    return processPart(part, workflows, rule.destination);
                }
            }
        }
        return "R";
    }

    @AllArgsConstructor
    static class Workflow {
        String name;
        List<Rule> rules;
    }

    @Data
    @AllArgsConstructor
    static class Range {
        int min;
        int max;

        Range intersect(Range other) {
            return new Range(Math.max(this.min, other.min), Math.min(this.max, other.max));
        }

        boolean overlaps(Range other) {
            return this.min <= other.max && this.max >= other.min;
        }

        Range exclude(Range other) {
            if (!this.overlaps(other)) {
                return this;
            }

            if (other.min <= this.min) {
                if (other.max >= this.max) {
                    return null;
                } else {
                    return new Range(other.max + 1, this.max);
                }
            } else if (other.max >= this.max) {
                return new Range(this.min, other.min - 1);
            } else {
                return null; // won't happen
            }
        }

        long count() {
            return max - min + 1;
        }
    }

    static class Rule {
        Predicate<Part> condition; // part 1
        String destination;
        Range xCondition, mCondition, aCondition, sCondition;

        Rule(String destination) {
            this.destination = destination;
        }

        Rule(String conditionStr, String destination) {
            var conditionParts = conditionStr.split("(>|<)");
            var attribute = conditionParts[0];
            var operator = conditionStr.contains(">") ? ">" : "<";
            var value = Integer.parseInt(conditionParts[1]);

            this.condition = part -> part.evaluate(attribute, value, operator);
            this.destination = destination;
            switch (attribute) {
                case "x":
                    xCondition = operator.equals(">") ? new Range(value + 1, MAX_RANGE) : new Range(MIN_RANGE, value - 1);
                    break;
                case "m":
                    mCondition = operator.equals(">") ? new Range(value + 1, MAX_RANGE) : new Range(MIN_RANGE, value - 1);
                    break;
                case "a":
                    aCondition = operator.equals(">") ? new Range(value + 1, MAX_RANGE) : new Range(MIN_RANGE, value - 1);
                    break;
                case "s":
                    sCondition = operator.equals(">") ? new Range(value + 1, MAX_RANGE) : new Range(MIN_RANGE, value - 1);
                    break;
            }
        }
    }

    @AllArgsConstructor
    static class Part {
        int x, m, a, s;

        boolean evaluate(String attribute, int value, String operator) {
            switch (attribute) {
                case "x":
                    return operator.equals(">") ? x > value : x < value;
                case "m":
                    return operator.equals(">") ? m > value : m < value;
                case "a":
                    return operator.equals(">") ? a > value : a < value;
                case "s":
                    return operator.equals(">") ? s > value : s < value;
                default:
                    throw new UnsupportedOperationException(); // won't happen
            }
        }

        int sumAttributes() {
            return x + m + a + s;
        }
    }

}
