package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.CollectableEntities.Armour;
import dungeonmania.response.models.AnimationQueue;


public class ZombieToast extends MovingEntity {
    private Armour armour;

    /**
     * Constructor for zombie_toast.
     * @param id
     * @param type
     * @param pos
     */
    public ZombieToast(String id, String type, Position pos) {
        super(id, type, pos, 10, 1);
        if (getType().equals("zombie_toast")) {
            this.armour = generateArmour();
        }
    }

    /**
     * Randomly generates armour.
     * @return
     */
    public Armour generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        if (num >= 7) {
            return new Armour("" + System.currentTimeMillis(), "armour", null);
        }
        return null;
    }

    /**
     * Selects a random direction and returns that direction.
     * @return
     */
    public Direction Randomdirection() {
        int num = ThreadLocalRandom.current().nextInt(0,4);
        // num = 0,1,2,3
        Direction direction = null;
        switch (num) {
            case 0:
                direction =  Direction.RIGHT;
            case 1:
                direction =  Direction.UP;
            case 2:
                direction =  Direction.LEFT;
            case 3:
                direction =  Direction.DOWN;
        }
        return direction;
    }

    /**
     * Move the zombie on the map, in a random direction.
     */
    public void move(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
        List<Entity> entities = map.get(super.getPlayerPos()).stream()
                                                             .filter(e -> e.isType("player"))
                                                             .collect(Collectors.toList());
        Player player = (Player) entities.get(0);

        if (player.getInvincDuration() > 0 && !player.getBattle().getDifficulty().equals("hard")) {
            moveAway(map, animations);
        }
        else {
            moveNormal(map, animations);
        }
    }

    /**
     * Moves the zombie in its tick phase. If the random direction choosen cannot
     * be passed by the zombie, the zombie does nothing
     * @param map
     */
    public void moveNormal(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
        Direction direction  = Randomdirection();
        Position newPos = super.getPos().translateBy(direction);
        if (canPass(map, newPos)) {
            super.moveInDir(map, direction);
            AnimationUtility.translateMovingEntity(animations, false, this, direction);
        }
    }

    /**
     * The function to move the zombie away from the player.
     * @param map
     */
    public void moveAway(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
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
        
        Direction direction = Position.getTranslationDirection(pos, newPos);
        AnimationUtility.translateMovingEntity(animations, false, this, direction);

        this.moveToPos(map, newPos.asLayer(3));
    }
    
    /**
     * Checks if the zombie can move onto a new position.
     */
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        if (!map.containsKey(pos)) {
            return false;
        }
        return map.get(pos.asLayer(1)).isEmpty();
    }

    /**
     * Checks if the zombie has armour or not.
     */
    public boolean hasArmour() {
        return this.armour != null;
    }

    // ********************************************************************************************\\
    //                                   Getter and setters:                                       \\
    // ********************************************************************************************\\
    
    public Armour getArmour() {
        Armour armour = this.armour;
        return armour;
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }
}
