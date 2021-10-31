package dungeonmania.CollectableEntities;

import dungeonmania.Battles.Battle;
import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    /**
     * Constructor for InvincibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvincibilityPotion(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Activates the ability of the potion.
     * Called when the player wants to consume the potion.
     */
    public void use() {
        getPlayer().setInvincDuration(30);
        getPlayer().getInventoryList().remove((CollectableEntity)this);
    }    
}