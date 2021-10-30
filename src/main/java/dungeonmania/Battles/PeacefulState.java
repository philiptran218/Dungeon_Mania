package dungeonmania.Battles;

import dungeonmania.MovingEntities.*;

import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.CollectableEntities.*;

public class PeacefulState implements BattleState {
    
    private Battle battle;

    public PeacefulState(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    /**
     * Since the difficulty is on Peaceful, only the player can attack.
     */
    public MovingEntity fight(Player p1, MovingEntity p2) {

        while (p2.getHealth() > 0) {
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
        }
        spawnOneRing(p1);
        p1.lootBody(p2);
        return p2;
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
        if (p2 instanceof Mercenary) {
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

    public void spawnOneRing(Player p1) {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 10% chance that The One Ring spawns after winning a battle
        if (num > 8) {
            TheOneRing ring = new TheOneRing("" + System.currentTimeMillis(), "one_ring", null);
            p1.getInventory().put(ring, p1);
        }
    }

}