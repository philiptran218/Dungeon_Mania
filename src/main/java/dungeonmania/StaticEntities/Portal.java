package dungeonmania.StaticEntities;

import java.util.Arrays;
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
        setType(getPortalColour(portalId));
    }


    public void teleport(Map<Position, List<Entity>> map, MovingEntity entity, Direction direction) {
        Position newPos = teleportPos.asLayer(3);
        entity.moveToPos(map, newPos);
    }


    public Portal getOtherPortal() {
        return otherPortal;
    }


    public void setOtherPortal(Portal otherPortal) {
        this.otherPortal = otherPortal;
    }

    /**
     * Set the colour of the portal:
     * 1 --> Blue
     * 2 --> Red
     * 3 --> Yellow
     * 4 --> Grey
     * @param id
     */
    public static String getPortalColour(int id) {
        switch (id) {
            case 1:
                return "BLUE";
            case 2:
                return "RED";
            case 3:
                return "YELLOW";
            case 4:
                return "GREY";
            default: 
                return null;
        }
    }

    public static int colourToId(String colour) {
        switch (colour) {
            case "BLUE":
                return 1;
            case "RED":
                return 2;
            case "YELLOW":
                return 3;
            case "GREY":
                return 4;
            default: 
                return 0;
        }
    }

    // List of all colours of the portal:
    public static List<String> portalCOlours() {
        return Arrays.asList("BLUE", "RED", "YELLOW", "GREY");
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