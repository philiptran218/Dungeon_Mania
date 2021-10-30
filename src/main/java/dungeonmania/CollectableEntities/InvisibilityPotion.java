package dungeonmania.CollectableEntities;

import dungeonmania.Battles.Battle;
import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    private boolean isActive;
    private int duration;
    private Battle battle;

    /**
     * Constructor for InvisibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvisibilityPotion(String id, String type, Position pos, Battle battle) {
        super(id, type, pos);
        this.setIsActive(false);
        this.setBattle(battle);
        this.setDuration(0);
    }

    // Getters and Setters
    public void setIsActive(boolean bool) {
        isActive = bool;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setDuration(int time) {
        this.duration = time;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * Activates the ability of the potion.
     * Called when the player wants to consume the potion.
     */
    public void use() {
        if (!getIsActive()) {
            setIsActive(true);
            this.setDuration(30);
            getBattle().setBattleState(getBattle().getInvisibleState());
        }   
    }

    /**
     * Good idea to keep a boolean in player, that tracks if the player is invincible or
     * invisible (isInvincible, isInvisible)
     */
    public void tickDuration() {
        this.setDuration(getDuration() - 1);

        if (getDuration() == 0) {
            setIsActive(false);

            // Check if there is a currently active invincibility potion
            if (!activeInvincPotion() || getBattle().getDifficulty().equals("hard")) {
                getBattle().setInitialState();
                // Set movement here as well....
            }
            // Otherwise, there is an active potion
            else {
                getBattle().setBattleState(getBattle().getInvincibleState()); 
                // Set invincible movement here...
            }
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }

    public Boolean activeInvincPotion() {
        InvincibilityPotion invincPotion = (InvincibilityPotion) getPlayer().getItem("invincibility_potion");
        if (invincPotion == null || !invincPotion.getIsActive()) {
            return false;
        }
        return true;
    }
}