package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity {
    /** 
     * Constructor for Boulder
     * @param id
     * @param type
     * @param pos
     */
    public Boulder(String id, String type, Position pos) {
        super(id, type, pos);
    }
    /** 
    * Checks if a boulder can be pushed
    * @param map
    * @param pos
    * @param direction
    * @return
    */
    public boolean canBePushed(Map<Position, List<Entity>> map, Direction direction) {
        Position pos = super.getPos();
        Position newPos = pos.translateBy(direction);
        for (int i = 1; i < 5; i++) {
            if (!map.get(new Position(newPos.getX(), newPos.getY(), i)).isEmpty()) {
                // New tile is not empty, cannot be pushed onto
                return false;
            }
        }
        // Can be pushed, push boulder
        return true;
    }

    /**
     * Pushes a boulder in a direction, updates map
     * @param map
     * @param direction
     */
    public void push(Map<Position, List<Entity>> map, Direction direction) {
        Position pos = super.getPos();
        Position newPos = pos.translateBy(direction);
        map.get(pos).clear();
        map.get(newPos).add(this);
        super.setPos(newPos);
    }
}
