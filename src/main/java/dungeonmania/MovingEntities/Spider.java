package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public class Spider extends MovingEntity {
    private List<Position> path = new ArrayList<Position>();
    private Position startPos;
    private boolean clockwise = true; // 1 for clockwise
    private int pathPos = 0;

    /**
     * Constructor for spider.
     * @param id
     * @param type
     * @param pos
     */
    public Spider(String id, String type, Position pos){
        super(id, type, pos, 5, 1);
        if (startPos == null) { this.startPos = pos; }
        this.setPath();
    }

    /**
     * Automated move in a circular path, also checks if the 
     * move is valid or not.
     */
    public void move(Map<Position, List<Entity>> map) {
        List<Entity> entities = map.get(super.getPlayerPos()).stream()
                                                             .filter(e -> e.getType().equals("player"))
                                                             .collect(Collectors.toList());
        System.out.println(entities);
        Player player = (Player) entities.get(0);

        if (player.getInvincDuration() > 0 && !player.getBattle().getDifficulty().equals("hard")) {
            moveAway(map);
        }
        else {
            moveNormal(map);
        }
    }

    /**
     * Normal Spider movement in clockwise direction.
     * @param map
     */
    public void moveNormal(Map<Position, List<Entity>> map) {
        if (super.getPos().equals(startPos)) {
            // Spider has just spawned, move up
            Position newPos = this.path.get(0);
            super.moveToPos(map, newPos);
            return;
        }

        // Set forward and backward move to 0-7 range
        int forwardMove = Math.floorMod(pathPos + 1, 8);
        int backwardMove = Math.floorMod(pathPos - 1, 8);

        boolean canMoveForward = canPass(map, this.path.get(forwardMove));
        boolean canMoveBackward = canPass(map, this.path.get(backwardMove));

        if (!canMoveForward && !canMoveBackward) {
            // Spider is trapped, it does not move
            this.clockwise = true;
        } else if (!canMoveForward) {
            // Spider can only go anti clockwise
            this.clockwise = false;
            pathPos = backwardMove;
            super.moveToPos(map, this.path.get(backwardMove));
        } else if (!canMoveBackward) {
            // Spider can only go clockwise
            this.clockwise = true;
            pathPos = forwardMove;
            super.moveToPos(map, this.path.get(forwardMove));
        } else {
            // can move in either direction
            if (clockwise) {
                // Spider was previously moving clockwise, it moves clockwise
                pathPos = forwardMove;
                super.moveToPos(map, this.path.get(forwardMove));
            } else {
                // Spider was previously moving anti clockwise, it moves anti clockwise
                pathPos = backwardMove;
                super.moveToPos(map, this.path.get(backwardMove));
            }
        }
    }

    /**
     * Move away from player, spider will stay on its circular path
     * @param map
     */
    public void moveAway(Map<Position, List<Entity>> map) {
        Position playerPos = this.getPlayerPos();
        Position pos = super.getPos();
        
        List<Position> cardinallyAdjacentPos = path.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
        cardinallyAdjacentPos.add(pos);

        int distance = Integer.MIN_VALUE;
        Position newPos = pos;

        for (Position tempPos: cardinallyAdjacentPos) {
            if (Position.distance(playerPos, tempPos) > distance && this.canPass(map, tempPos)) {
                newPos = tempPos;
                distance = Position.distance(playerPos, tempPos);
            }
        }

        this.moveToPos(map, new Position(newPos.getX(), newPos.getY(), 3));
    }

    /**
     * Checks if the spider is able to move onto the given position.
     * @return True is the spider can move onto the pos, false otherwise.
     */
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return !isPassingBoulder(map, pos);
    }

    /**
     * Set the circular path that the spider takes, and append all 
     * positions to a list.
     */
    public void setPath() {
        path.clear();
        this.path.add(startPos.translateBy(Direction.UP));
        this.path.add(startPos.translateBy(Direction.UP).translateBy(Direction.RIGHT));
        this.path.add(startPos.translateBy(Direction.RIGHT));
        this.path.add(startPos.translateBy(Direction.RIGHT).translateBy(Direction.DOWN));
        this.path.add(startPos.translateBy(Direction.DOWN));
        this.path.add(startPos.translateBy(Direction.DOWN).translateBy(Direction.LEFT));
        this.path.add(startPos.translateBy(Direction.LEFT));
        this.path.add(startPos.translateBy(Direction.LEFT).translateBy(Direction.UP));
    }

    /**
     * Spider cannot have armour.
     */
    public boolean hasArmour() {
        return false;
    }

    // Override the toJSONObject Function
    @Override
    public JSONObject toJSONObject() {
        JSONObject tmp = super.toJSONObject();
        JSONObject centre = new JSONObject();
        centre.put("x", startPos.getX());
        centre.put("y", startPos.getY());
        tmp.put("centre", centre);
        return tmp;
    }

    /**
     * Set the starting position of the spider
     */
    public void setStartPosition(Position start) {
        this.startPos = start;
        this.setPath();
        // Find its current position
        pathPos = path.indexOf(super.getPos());
    }
}