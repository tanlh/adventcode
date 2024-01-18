package _2022;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import util.Util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Day11 {

    static int ROUND = 10000;
    static Pattern opPattern = Pattern.compile("new\\s*=\\s*old\\s*([+\\-*/])\\s*(\\d+|old)");

    @Data
    static class Monkey {
        String id, next1, next2;
        long divide, inspected = 0L;
        Queue<Long> inputs;
        Function<Long, Long> operation;

        Monkey(List<String> rule) {
            this.id = rule.get(0).substring(7, 8);
            this.inputs = new LinkedList<>(Util.parseLine(rule.get(1).split(":")[1], ",", Long::parseLong));
            this.divide = Long.parseLong(StringUtils.right(rule.get(3), 2).trim());
            this.next1 = StringUtils.right(rule.get(4), 1);
            this.next2 = StringUtils.right(rule.get(5), 1);

            var matcher = opPattern.matcher(rule.get(2).split(": ")[1]);
            matcher.find();
            this.operation = x -> {
                var value = "old".equals(matcher.group(2)) ? x : Long.parseLong(matcher.group(2));
                // part 1
//                return ("*".equals(matcher.group(1)) ? x * v : x + v) / 3;
                return "*".equals(matcher.group(1)) ? x * value : x + value;
            };
        }
    }

    public static void main(String[] args) {
        var monkeys = Util.readFileToBlocks().stream().map(Monkey::new).toList();
        var monkeyMap = Maps.uniqueIndex(monkeys, Monkey::getId);
        var modulus = monkeys.stream().map(Monkey::getDivide).reduce(1L, (i1, i2) -> i1 * i2);

        for (int i = 0; i < ROUND; i++) {
            for (var monkey : monkeys) {
                while (!monkey.inputs.isEmpty()) {
                    monkey.inspected++;
                    var newItem = monkey.operation.apply(monkey.inputs.poll());
                    newItem = newItem % modulus;
                    monkeyMap.get(newItem % monkey.divide == 0L ? monkey.next1 : monkey.next2).inputs.offer(newItem);
                }
            }
        }

        var result = monkeys.stream().map(Monkey::getInspected)
            .sorted(Comparator.reverseOrder())
            .limit(2)
            .reduce(1L, (i1, i2) -> i1 * i2);
        System.err.println("Result: " + result);
    }

}