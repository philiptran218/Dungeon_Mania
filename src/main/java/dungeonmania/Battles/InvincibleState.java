package dungeonmania.Battles;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.ZombieToast;

public class InvincibleState implements BattleState {

    /**
     * Since the player is invincible, they will win any fight immediately. This will
     * still reduce the durability of the player's weapons.
     */
    public void fight(Player p1, MovingEntity p2) {
        usedWeapons(p1);
        usedArmour(p2);
        p2.setHealth(0);
    }

     /**
     * Reduces durability of the player's weapons since they are still 'used' in
     * battle. Since battles end immediately, no point calculating damage.
     * @param p1
     */
    public void usedWeapons(Player p1) {
        Sword sword = (Sword) p1.getInventory().getItem("sword");
        Bow bow = (Bow) p1.getInventory().getItem("bow");

        if (sword != null) {
            sword.usedInCombat();
        }
        if (bow != null) {
            bow.usedInCombat();
        }
    }

    /**
     * Reduces durability of the enemy's armour (if they have armour).
     * @param p2
     */
    public void usedArmour(MovingEntity p2) {
        if (p2 instanceof Mercenary) {
            Mercenary merc = (Mercenary) p2;
            Armour armour = merc.getArmour();

            if (armour != null) {
                armour.reduceDurability();
                // Remove armour if it is broken.
                if (armour.getDurability() == 0) {
                    //merc.setArmour(null);
                }
            }
        }
        else if (p2 instanceof ZombieToast) {
            ZombieToast zombie = (ZombieToast) p2;
            Armour armour = zombie.getArmour();

            if (armour != null) {
                armour.reduceDurability();
                // Remove armour if it is broken.
                if (armour.getDurability() == 0) {
                    //merc.setArmour(null);
                }
            }
        }
    }
}