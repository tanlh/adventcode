package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Node is mainly used for find path problems
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    public int x, y, steps, weight;
    public char direction;
    public Node previous;

    public Node(int x, int y) {
        this(x, y, 0, 0, ' ', null);
    }

}