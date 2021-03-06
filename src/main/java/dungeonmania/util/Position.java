package dungeonmania.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Position {
    private final int x, y, layer;

    public Position(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.layer = 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(x, y, layer);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;

        // z doesn't matter
        return x == other.x && y == other.y;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getLayer() {
        return layer;
    }

    public final Position asLayer(int layer) {
        return new Position(x, y, layer);
    }

    public final Position translateBy(int x, int y) {
        return this.translateBy(new Position(x, y));
    }

    public final Position translateBy(Direction direction) {
        return this.translateBy(direction.getOffset());
    }

    public final Position translateBy(Position position) {
        return new Position(this.x + position.x, this.y + position.y, this.layer + position.layer);
    }

    // (Note: doesn't include z)

    /**
     * Calculates the position vector of b relative to a (ie. the direction from a
     * to b)
     * @return The relative position vector
     */
    public static final Position calculatePositionBetween(Position a, Position b) {
        return new Position(b.x - a.x, b.y - a.y);
    }

    public  static final boolean isAdjacent(Position a, Position b) {
        int x = a.x - b.x;
        int y = a.y - b.y;
        return x + y == 1;
    }

    // (Note: doesn't include z)
    public final Position scale(int factor) {
        return new Position(x * factor, y * factor, layer);
    }

    @Override
    public final String toString() {
        return "Position [x=" + x + ", y=" + y + ", z=" + layer + "]";
    }

    // Return Adjacent positions in an array list with the following element positions:
    // 0 1 2
    // 7 p 3
    // 6 5 4
    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x-1, y-1));
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    // Our own added Methods
    /**
     * Gets the square of the distance between 2 positions, ignoring the layer component
     * @return  The square of the distance between 2 positions
     */
    public static int distance(Position a, Position b) {
        return (a.getX() - b.getX())*(a.getX() - b.getX()) + (a.getY() - b.getY())*(a.getY() - b.getY());
    }
    
    /**
     * Checks if 2 Positions are cardinally adjacent (up, down, left, right). Ignores layer
     * @return true if the positions are cardinally adjacent, false otherwise
     */
    public static boolean isCardinallyAdjacent(Position a, Position b) {
        return distance(a, b) == 1;
    }
    public List<Position> getCardinallyAdjacentPositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }
    public static Direction getTranslationDirection(Position oldPos, Position newPos) {
        Position translationDirection = new Position(newPos.getX() - oldPos.getX(), newPos.getY() - oldPos.getY());
        if (translationDirection.getX() == 0) {
            if (translationDirection.getY() == 1) {
                return Direction.DOWN;
            }
            if (translationDirection.getY() == -1) {
                return Direction.UP;
            }
        }
        if (translationDirection.getY() == 0) {
            if (translationDirection.getX() == 1) {
                return Direction.RIGHT;
            }
            if (translationDirection.getX() == -1) {
                return Direction.LEFT;
            }
        }
        return Direction.NONE;
    }
}
