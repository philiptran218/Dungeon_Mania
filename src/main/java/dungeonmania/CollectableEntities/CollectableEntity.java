package dungeonmania.CollectableEntities;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {

    private Player player;
    /**
     * Constructor for CollectableEntity
     * @param id
     * @param type
     * @param pos
     */
    public CollectableEntity(String id, String type, Position pos) {
        super(id, type, pos);
    }

    // Getters and Setters
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Player picks up the item when they walk over it, defeat an enemy that has the
     * item or builds the item.
     * @param player
     */
    public void pickUp(Player player) {
        this.setPos(null);
        this.setPlayer(player);
        
        // this.getPlayer().getInventory.add(this);
    }

    abstract public void use();
}