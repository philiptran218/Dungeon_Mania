package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    private boolean isActive;
    // Need to hold battle class and movement class

    /**
     * Constructor for InvincibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvincibilityPotion(String id, String type, Position pos) {
        super(id, type, pos);
        this.setIsActive(false);
    }

    // Getters and Setters
    public void setIsActive(boolean bool) {
        isActive = bool;
    }

    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Activates the ability of the potion.
     * Called when the player wants to consume the potion.
     */
    public void use() {
        setIsActive(true);
        // Have to change state of movement here for enemies
        // Also change state here for combat

        // Enemies move away from the player
        // Combat kills enemies immediately (set health to 0)

        // HAVE TO CHECK FOR DIFFICULTY, IF ON HARD, USING POTION DOES NOTHING
        // (DON'T CHANGE ANY STATE)
    }
    
}