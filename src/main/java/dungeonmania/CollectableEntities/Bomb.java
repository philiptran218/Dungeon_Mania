package dungeonmania.CollectableEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Bomb extends CombatItems {
    private static final int EXPLOSION_RADIUS = 2;
    public Bomb(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Places the bomb onto the map, and removes it from inventory.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }

    public void detonate(Map<Position, List<Entity>> map) {
        for(Position tempPos : map.keySet()) {
            if (Math.sqrt(Position.distance(getPos(), tempPos)) < EXPLOSION_RADIUS) {
                // Within explosion radius
                List<Entity> entities = map.get(tempPos);
                Entity player = getPlayer(entities);
                if (player != null) {
                    entities.clear();
                    entities.add(player);
                }   else {
                    entities.clear();
                }
            }
        }
    }

    public Entity getPlayer(List<Entity> entities) {
        if (entities.size() == 0) {
            return null;
        }
        Entity player = null;
        for (Entity entity: entities) {
            if (entity.getType().equals("player")) {
                return player;
            }
        }
        return null;
    }

}