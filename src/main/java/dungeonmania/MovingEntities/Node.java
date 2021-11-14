package dungeonmania.MovingEntities;

import java.util.Comparator;

import dungeonmania.util.Position;

public class Node implements Comparator<Node> {
    private Position pos;
    private double dist;

    public Node() {}

    /**
     * Node is a helper class to utilise PriorityQueue
     * @param pos
     * @param dist
     */
    public Node(Position pos, double dist) {
        this.pos = pos;
        this.dist = dist;
    }

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.dist > node2.dist) {
            return 1; 
        } else if (node1.dist < node2.dist) {
            return -1;  
        } else {
            return 0;
        }
    }

    public Position getPos() {
        return pos;
    }
}
