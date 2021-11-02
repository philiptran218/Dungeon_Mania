package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
// When doing a teleport, set the player location to do teleportPos

public class Portal extends StaticEntity {
    private Position teleportPos;
    private String colour;
    /**
     * Constructor for Portal
     * @param id
     * @param type
     * @param pos
     */
    public Portal(String id, String type, Position pos, String colour) {
        super(id, type, pos);
        this.colour = colour;
        setType(getPortalColour() + type);
    }


    public void teleport(Map<Position, List<Entity>> map, MovingEntity entity, Direction direction) {
        Position newPos = teleportPos.asLayer(3);
        entity.moveToPos(map, newPos);
    }

    /**
     * Getter for teleportPos
     * @return where the portal leads to 
     */
    public Position getTeleportPos(Map<Position, List<Entity>> map, Direction direction) {
        for (Position keys : map.keySet()) {
            if (map.get(keys).size() == 1) {
                Entity entity = map.get(keys).get(0);
                if (entity instanceof Portal && ((Portal) entity).getPortalColour().equals(colour) && !this.getPos().equals(entity.getPos())) {
                    // Other portal
                    this.teleportPos = entity.getPos().translateBy(direction);
                    return entity.getPos().translateBy(direction);
                }
            }
        }

        // Should not occur
        return null;
    }

    /**
     * Return the portal id.
     * @return
     */
    public String getPortalColour() {
        return colour;
    }

}