package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class TheOneRing extends CombatItems {

    /**
     * Constructor for TheOneRing
     * @param id
     * @param type
     * @param pos
     */
    public TheOneRing(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * If the player moves onto TheOneRing, it is added to their
     * inventory.
     * @param player
     */
    public void pickUp(Player player) {
        this.setPlayer(player);
        
        // Add the ring to player inventory here
        // Also maybe remove it from Map's list of entities.
    }

    /**
     * Revives the player if their health falls to 0. Removing TheOneRing from
     * inventory should be done separately.
     */
    public void revive() {
        this.getPlayer().setHealth(100);
    }

}