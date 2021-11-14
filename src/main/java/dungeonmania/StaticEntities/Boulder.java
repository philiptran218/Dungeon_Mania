package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.response.models.AnimationQueue;
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
        if (map.get(newPos) == null) {
            return false;
        }
        for (int i = 1; i < 5; i++) {
            if (!map.get(newPos.asLayer(i)).isEmpty()) {
                // New tile is not empty, cannot be pushed onto
                return false;
            }
        }
        // Checks if it is in the swamp tile list:
        for (Entity e : map.get(super.getPos().asLayer(0))) {
            if (e.isType("swamp_tile") && ((SwampTile) e).entityOnTile(super.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pushes a boulder in a direction, detonates a bomb if boulder goes onto a switch
     * @param map
     * @param direction
     */
    public void push(Map<Position, List<Entity>> map, Direction direction, List<AnimationQueue> animations) {
        Position pos = super.getPos(); // Boulder position
        Position newPos = pos.translateBy(direction);

        AnimationUtility.translateBoulder(animations, direction, getId());

        map.get(pos).clear();
        map.get(newPos).add(this);
        super.setPos(newPos);
    }
}