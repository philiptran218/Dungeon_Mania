package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class Treasure extends Utility {

    /**
     * Constructor for Treasure
     * @param id
     * @param type
     * @param pos
     */
    public Treasure (String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Uses treasure to build shields or bribe mercenaries. 
     */
    public void use() {
        getPlayer().getInventoryList().remove((CollectableEntity)this);
    }

}