package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
// When doing a teleport, set the player location to do teleportlocation

public class Portal extends StaticEntity {
    /**
     * Constructor for Portal
     * @param id
     * @param type
     * @param pos
     */
    public Portal(String id, String type, Position pos) {
        super(id, type, pos);
    }
}
