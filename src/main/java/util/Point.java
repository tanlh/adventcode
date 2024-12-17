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

}
