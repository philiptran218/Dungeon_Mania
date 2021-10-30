package dungeonmania.Battles;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.ZombieToast;

public class NormalState implements BattleState {
    
    private Battle battle;
    
    /**
     * This is treated as BattleState for Normal and Hard difficulty.
     * @param battle
     */
    public NormalState(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    /**
     * Conducts the normal actions for fighting. p1 is the player and
     * p2 is the enemy.
     */
    public void fight(Player p1, MovingEntity p2) {

        while (p1.getHealth() > 0 && p2.getHealth() > 0) {
            // Player attacks enemy first
            healthModifier(p2, damageCalculation(p1), p1.getHealth());

            // Performs a second attack
            if (p2.getHealth() > 0 && this.getBattle().hasBow(p1)) {
                healthModifier(p2, damageCalculation(p1), p1.getHealth());
            }
            
            // Performs an attack if player has an allied mercenary
            for (MovingEntity merc : p1.getBribedMercenaries()) {
                if (p2.getHealth() > 0) {
                    healthModifier(p2, damageCalculation(merc), merc.getHealth());
                }
            }

            // Then enemy attacks player (if the enemy is still alive)
            if (p2.getHealth() > 0) {
                healthModifier(p1, damageCalculation(p2), p2.getHealth());
            }
        }

    }

    /**
     * Checks for base dmg, swords and bows that can modify damage.
     * @param p1
     */
    public double damageCalculation(MovingEntity p1) {
        if (p1 instanceof Player) {
            Player plyr = (Player) p1;
            double dmg = plyr.getAttackDamage();
            Sword sword = (Sword) plyr.getInventory().getItem("sword");
            Bow bow = (Bow) plyr.getInventory().getItem("bow");

            if (sword != null) {
                dmg += sword.usedInCombat();
            }
            if (bow != null) {
                dmg += bow.usedInCombat();
            }
            return dmg;
        }
        else {
            return p1.getAttackDamage();
        }
    }

    public void healthModifier(MovingEntity p2, double dmg, double health) {
        double newHealth;
        if (p2 instanceof Player) {
            Player plyr = (Player) p2;
            Shield shield = (Shield) plyr.getInventory().getItem("shield");
            Armour armour = (Armour) plyr.getInventory().getItem("armour");
            double multiplier = 1;

            if (shield != null) {
                multiplier *= shield.usedInCombat();
            }
            if (armour != null) {
                multiplier *= armour.usedInCombat();
            }
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 10);
        }
        else if (p2 instanceof Mercenary) {
            Mercenary merc = (Mercenary) p2;
            Armour armour = merc.getArmour();
            double multiplier = 1;

            if (armour != null) {
                multiplier *= armour.getReduceDamage();
                armour.reduceDurability();

                // Remove armour if it is broken.
                if (armour.getDurability() == 0) {
                    merc.setArmour(null);
                }
            }
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2 instanceof ZombieToast) {
            ZombieToast zombie = (ZombieToast) p2;
            Armour armour = zombie.getArmour();
            double multiplier = 1;

            if (armour != null) {
                multiplier *= armour.getReduceDamage();
                armour.reduceDurability();

                // Remove armour if it is broken.
                if (armour.getDurability() == 0) {
                    zombie.setArmour(null);
                }
            }
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else {
            newHealth = p2.getHealth() - ((health * dmg) / 5);
        }
        p2.setHealth(newHealth);
    }

}
