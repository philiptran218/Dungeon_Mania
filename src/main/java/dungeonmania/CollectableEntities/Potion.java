package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public abstract class Potion extends CollectableEntity {

    private Player player;
    /**
     * Constructor for Potion
     * @param id
     * @param type
     * @param pos
     */
    public Potion(String id, String type, Position pos) {
        super(id, type, pos);
    }

    // Getters and Setters
    public void setPlayer(Player plyr) {
        player = plyr;
    }

    public Player getPlayer() {
        return player;
    }
}