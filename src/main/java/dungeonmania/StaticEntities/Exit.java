package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;

public class Exit extends StaticEntity{
    /**
     * Constructor for Exit
     * @param id
     * @param type
     * @param pos
     */
    public Exit(String id, String type, Position pos) {
        super(id, type, pos);
        super.setType("exit");
    }
    /**
     * Checks if there is an exit under the player
     * @param map
     * @return
     */
    public boolean isUnderPlayer(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        List<Entity> entities = map.get(new Position(pos.getX(), pos.getY(), 3));
        if (entities.isEmpty()) {
            return false;
        }
        for (Entity entity : entities) {
            if (entity.getType().equals("player")) {
                return true;
            }
        }
        return false;
    }
}
