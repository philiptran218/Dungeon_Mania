package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public class ZombieToast extends MovingEntity implements MovingEntityObserver{
    private boolean armour;

    public ZombieToast(String id, String type, Position pos) {
        super(id, type, pos, 4, 4);
        this.armour = generateArmour();

    }

    public boolean generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        return num >= 7;
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

    /**
     * Moves the zombie in its tick phase. If the random direction choosen cannot
     * be passed by the zombie, the zombie does nothing
     * @param map
     */
    public void move(Map<Position, List<Entity>> map) {
        Direction direction  = Randomdirection();
        Position newPos = super.getPos().translateBy(direction);
        if (canPass(map, newPos)) {
            super.moveInDir(map, direction);
        }

    }

    
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        List<Entity> entities = map.get(pos);
        for (Entity entity: entities) {
            switch(entity.getType()) {
                case "wall":
                    return false;
                case "boulder":
                    return false;
                case "door":
                    return false;
            }
        }
        return true;
    }

    @Override
    public void update(MovingEntitySubject obj) {
        super.setPlayerLocation(((Player) obj).getPos());
    }
}
