package dungeonmania.CollectableEntities;

import dungeonmania.Battles.Battle;
import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    private boolean isActive;
    private int duration;
    private Battle battle;

    /**
     * Constructor for InvincibilityPotion
     * @param id
     * @param type
     * @param pos
     */
    public InvincibilityPotion(String id, String type, Position pos, Battle battle) {
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
        if (!getIsActive() && !activeInvisPotion() && !getBattle().getDifficulty().equals("hard")) {
            setIsActive(true);
            this.setDuration(30);
            getBattle().setBattleState(getBattle().getInvincibleState());
        } 
        else if (!getIsActive()) {
            setIsActive(true);
            this.setDuration(30);
        }
    }

    public void tickDuration() {
        this.setDuration(getDuration() - 1);

        if (getDuration() == 0) {
            setIsActive(false);

            // Check if there is a currently active invisibility potion
            if (!activeInvisPotion()) {
                getBattle().setInitialState();
                // Set movement here as well....
            }
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }

    public Boolean activeInvisPotion() {
        InvisibilityPotion invisPotion = (InvisibilityPotion) getPlayer().getItem("invisibility_potion");
        if (invisPotion == null || !invisPotion.getIsActive()) {
            return false;
        }
        return true;
    }
    
}