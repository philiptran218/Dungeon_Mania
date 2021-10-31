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
    private Portal otherPortal;
    private int portalId;
    /**
     * Constructor for Portal
     * @param id
     * @param type
     * @param pos
     */
    public Portal(String id, String type, Position pos, int portalId) {
        super(id, type, pos);
        this.portalId = portalId;
    }


    public void teleport(Map<Position, List<Entity>> map, MovingEntity entity, Direction direction) {
        Position newPos = teleportPos.translateBy(direction).asLayer(3);
        entity.moveToPos(map, newPos);
    }


    public Portal getOtherPortal() {
        return otherPortal;
    }


    public void setOtherPortal(Portal otherPortal) {
        this.otherPortal = otherPortal;
    }


    /**
     * Getter for teleportPos
     * @return where the portal leads to 
     */
    public Position getTeleportPos(Map<Position, List<Entity>> map, Direction direction) {
        for (Position keys : map.keySet()) {
            if (map.get(keys).size() == 1) {
                Entity entity = map.get(keys).get(0);
                if (entity.getType().equals("portal") && ((Portal) entity).getPortalId() == portalId && !this.getPos().equals(entity.getPos())) {
                    // Other portal
                    this.teleportPos = entity.getPos().translateBy(direction);
                    return entity.getPos().translateBy(direction);
                }
            }
        }

        // Should not occur
        return null;
    }


    public int getPortalId() {
        return portalId;
    }

}