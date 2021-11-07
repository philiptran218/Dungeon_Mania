package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.CollectableEntities.Bomb;
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
    public void push(Map<Position, List<Entity>> map, Direction direction) {
        Position pos = super.getPos(); // Boulder position
        Position newPos = pos.translateBy(direction);
        map.get(pos).clear();
        map.get(newPos).add(this);
        super.setPos(newPos);

        pos = super.getPos();

        List<Entity> entities = map.get(getPos().asLayer(0));
        if ( entities.size() > 0 && entities.get(0).getType().equals("switch")) {
            // Boulder is on a switch
            List<Position> adjacentPos = pos.getCardinallyAdjacentPositions();
            for (Position tempPos: adjacentPos) {
                // Checks if a bomb needs to be exploded
                List<Entity> collectablEntities = map.get(tempPos.asLayer(2));
                if (collectablEntities.size() > 0 && collectablEntities.get(0).getType().equals("bomb")) {
                    // contains bomb
                    Bomb bomb = (Bomb) collectablEntities.get(0);
                    bomb.detonate(map);
                }
            }
        }
    }

}