package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class Shield extends CombatItems {

    private static final double REDUCE_DAMAGE = 0.6;
    // Leave at 20 for now, change it later
    private static final int INITIAL_DURABILITY = 20;
    private int durability;
    
    /**
     * Constructor for Shield
     * @param id
     * @param type
     * @param pos
     */
    public Shield(String id, String type, Position pos) {
        super(id, type, pos);
        this.setDurability(INITIAL_DURABILITY);
    }

    // Getters and Setters
    public void setDurability(int num) {
        durability = num;
    }
    
    public int getDurability() {
        return durability;
    }

    public double getReduceDamage() {
        return REDUCE_DAMAGE;
    }

    /**
     * Reduces the durability if it has been used
     */
    public void reduceDurability() {
        durability = durability - 1;
    }

    /**
     * Won't need to be called as shields are only used in battles.
     */
    public void use() {
    }

    /**
     * Called when the combat item is being used in battle.
     * Its durability is reduced by 1 and its damage reduction value is returned for
     * use in damage calculation.
     * @return the damage count of the combat item
     */
    public double usedInCombat() {
        reduceDurability();
        checkNoDurability();
        return REDUCE_DAMAGE;
    }

    public void checkNoDurability() {
        if (durability == 0) {
            getPlayer().getInventoryList().remove((CollectableEntity)this);
        }
    }

}