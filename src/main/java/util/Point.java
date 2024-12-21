package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    public int x, y;

    public Point(Point point, int[] direction) {
        this.x = point.x + direction[0];
        this.y = point.y + direction[1];
    }

    /**
     * Coordinates separated by comma
     */
    public Point(String line) {
        var coordinates = Util.parseLine(line, ",", Integer::parseInt);
        this.x = coordinates.getFirst();
        this.y = coordinates.getLast();
    }

}
