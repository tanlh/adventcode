package _2023;

import util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Collectors;

public class Day24 {

    static Double min = 200000000000000D;
    static Double max = 400000000000000D;
//    static Double min = 7D;
//    static Double max = 27D;

    public static void main(String[] args) {
        var stones = Util.readFileToLines().stream().map(Stone::new).collect(Collectors.toList());

        int intersect = 0;
        for (var s1 : stones) {
            for (var s2 : stones) {
                if (!s1.equals(s2) && isIntersect2DInRange(s1, s2)) {
                    intersect++;
                }
            }
        }
        System.err.println("Part 1: " + (intersect / 2)); // counted twice

        // Part 2
        // Found solution by set of equations here: https://www.reddit.com/r/adventofcode/comments/18q40he/2023_day_24_part_2_a_straightforward_nonsolver/?rdt=42050
        var stone1 = stones.get(0);
//        var stone2 = stones.get(1);
        var stone2 = stones.get(2);

        System.err.printf("%sx + %sy + %svx + %svy = %s \n",
            (stone2.vy - stone1.vy), (stone1.vx - stone2.vx), stone1.y - stone2.y, stone2.x - stone1.x,
            stone2.x * stone2.vy - stone2.y * stone2.vx - stone1.x * stone1.vy + stone1.y * stone1.vx
        );
        System.err.printf("%sx + %sz + %svx + %svz = %s \n",
            (stone2.vz - stone1.vz), (stone1.vx - stone2.vx), (stone1.z - stone2.z), (stone2.x - stone1.x),
            (stone2.x * stone2.vz - stone2.z * stone2.vx - stone1.x * stone1.vz + stone1.z * stone1.vx)
        );
        System.err.printf("%sy + %sz + %svy + %svz = %s \n",
            (stone1.vz - stone2.vz), (stone2.vy - stone1.vy), (stone2.z - stone1.z), (stone1.y - stone2.y),
            (stone2.z * stone2.vy - stone2.y * stone2.vz - stone1.z * stone1.vy + stone1.y * stone1.vz)
        );

        // Got the set of 6 equations
        // -250x + 9y + 70283082145152vx + 13987421738712vy = -51609541826872561
        // 57x + 9z + 73062932262528vx + 13987421738712vz = 26804285232072291
        // -57y + -250z + -73062932262528vy + 70283082145152vz = -15202955658442189
        // -442x + -26y + -84343412344850vx + -8791705543396vy = -123981512414266340
        // -224x + -26z + -95197059164770vx + -8791705543396vz = -69805329865161028
        // 224y + -442z + 95197059164770vy + -84343412344850vz = -5552913149043282

        // Try Excel, but for large number with more than 15 digits, Excel rounds the number
        // Try Math from Java, but got non-integer number
        // Use sympy from python -> got correct result
        /**
         * Use this web: https://live.sympy.org/
         *
         * from sympy import Matrix, symbols, linsolve
         *
         * # Declare symbols for the variables
         * x, y, z, vx, vy, vz = symbols('x y z vx vy vz')
         *
         * # Coefficient matrix
         * A = Matrix([
         *     [-250, 9, 0, 70283082145152, 13987421738712, 0],
         *     [57, 0, 9, 73062932262528, 0, 13987421738712],
         *     [0, -57, -250, 0, -73062932262528, 70283082145152],
         *     [-442, -26, 0, -84343412344850, -8791705543396, 0],
         *     [-224, 0, -26, -95197059164770, 0, -8791705543396],
         *     [0, 224, -442, 0, 95197059164770, -84343412344850]
         * ])
         *
         * # Constant vector
         * B = Matrix([
         *     -51609541826872561,
         *     26804285232072291,
         *     -15202955658442189,
         *     -123981512414266340,
         *     -69805329865161028,
         *     -5552913149043282
         * ])
         *
         * # Solve the system
         * solution = linsolve((A, B), x, y, z, vx, vy, vz)
         *
         * # Print the solution
         * for sol in solution:
         *     print(sol)
         */

        // Result: 242369545669096, 339680097675927, 102145685363875, 107, -114, 304
        System.err.println("Pat 2: " + (242369545669096L + 339680097675927L + 102145685363875L));
    }

    private static boolean isIntersect2DInRange(Stone s1, Stone s2) {
        Double m1 = (double)s1.vy / s1.vx;
        Double m2 = (double)s2.vy / s2.vx;

        Double b1 = s1.y - m1 * s1.x;
        Double b2 = s2.y - m2 * s2.x;

        if (m1.compareTo(m2) == 0) {
            return false;
        }

        Double ix = (b2 - b1) / (m1 - m2);
        Double iy = m1 * ix + b1;

        Double t1 = (ix - s1.x) / s1.vx;
        Double t2 = (ix - s2.x) / s2.vx;
        if (t1.compareTo(0D) <= 0 || t2.compareTo(0D) <= 0) {
            return false;
        }

        return ix.compareTo(min) >= 0 && ix.compareTo(max) <= 0
            && iy.compareTo(min) >= 0 && iy.compareTo(max) <= 0;
    }

    @Data
    @AllArgsConstructor
    static class Stone {
        long x, y, z, vx, vy, vz;

        Stone(String config) {
            var parts = config.split("@");
            var position = Util.parseLine(parts[0], ",", Long::parseLong);
            var velocity = Util.parseLine(parts[1], ",", Long::parseLong);
            this.x = position.get(0);
            this.y = position.get(1);
            this.z = position.get(2);
            this.vx = velocity.get(0);
            this.vy = velocity.get(1);
            this.vz = velocity.get(2);
        }
    }
}
