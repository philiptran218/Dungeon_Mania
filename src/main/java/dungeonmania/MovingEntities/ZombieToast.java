package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;
import dungeonmania.CollectableEntities.Armour;


public class ZombieToast extends MovingEntity {
    private Armour armour;

    public ZombieToast(String id, String type, Position pos) {
        super(id, type, pos, 10, 1);
        this.armour = generateArmour();

    }

    public Armour generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        if (num >= 7) {
            return new Armour("" + System.currentTimeMillis(), "armour", null);
        }
        return null;
    }

    public Direction Randomdirection() {
        int num = ThreadLocalRandom.current().nextInt(0,4);
        // num = 0,1,2,3
        switch (num) {
            case 0:
                return Direction.RIGHT;
            case 1:
                return Direction.UP;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.DOWN;
        }
        return null;
    }

    public void move(Map<Position, List<Entity>> map) {
        List<Entity> entities = map.get(super.getPlayerPos()).stream()
                                                             .filter(e -> e.getType().equals("player"))
                                                             .collect(Collectors.toList());
        Player player = (Player) entities.get(0);

        if (player.getInvincDuration() > 0 && !player.getBattle().getDifficulty().equals("Hard")) {
            moveAway(map);
        }
        else {
            moveNormal(map);
        }
    }

    /**
     * Moves the zombie in its tick phase. If the random direction choosen cannot
     * be passed by the zombie, the zombie does nothing
     * @param map
     */
    public void moveNormal(Map<Position, List<Entity>> map) {
        Direction direction  = Randomdirection();
        Position newPos = super.getPos().translateBy(direction);
        if (canPass(map, newPos)) {
            super.moveInDir(map, direction);
        }
    }

    public void moveAway(Map<Position, List<Entity>> map) {
        Position playerPos = this.getPlayerPos();
        Position pos = super.getPos();
        
        List<Position> adjacentPos = pos.getAdjacentPositions();

        List<Position> cardinallyAdjacentPos = adjacentPos.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
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
    
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();  
    }

    public boolean hasArmour() {
        return this.armour != null;
    }
    public Armour getArmour() {
        Armour armour = this.armour;
        return armour;
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }
    
}
