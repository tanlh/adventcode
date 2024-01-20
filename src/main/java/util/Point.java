package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    public long x, y;

    public Point(List<Long> coordinate) {
        this.x = coordinate.get(0);
        this.y = coordinate.get(1);
    }

}
