package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    private boolean isActive;
    // Need to hold movement class for enemies

    /**
     * Constructor for InvisibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvisibilityPotion(String id, String type, Position pos) {
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

        // Change the state of movement here
        // Enemies cannot see player so they move normally
        // Create a thread for 30 secs, which sets the movement state
        // Once finished, it resets the movement state
    }
}