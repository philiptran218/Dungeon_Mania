package dungeonmania.StaticEntities;

import dungeonmania.util.Position;
// When doing a teleport, set the player location to do teleportlocation

public class Portal extends StaticEntity {
    private Position teleportLocation;
    /**
     * Constructor for portal
     */
    public Portal(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
        super.setType("Portal");
    }
    /**
     * Getter for teleportLocation
     * @return where the portal leads to 
     */
    public Position getTeleportLocation() {
        return teleportLocation;
    }
    /**
     * Setter for teleportLocation
     * @param teleportLocation
     */
    public void setTeleportLocation(Position teleportLocation) {
        this.teleportLocation = teleportLocation;
    }
}
