package dungeonmania.Battles;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.MovingEntities.Assassin;
import dungeonmania.MovingEntities.Hydra;

public class NormalState implements BattleState {
    
    private Battle battle;
    private Random rand = new Random(100032390);
    
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
    public MovingEntity fight(Player p1, MovingEntity p2) {

        while (p1.getHealth() > 0 && p2.getHealth() > 0) {
            // Player attacks enemy first
            healthModifier(p2, damageCalculation(p1, p2), p1.getHealth());

            // Performs a second attack
            if (p2.getHealth() > 0 && this.getBattle().hasBow(p1)) {
                healthModifier(p2, damageCalculation(p1, p2), p1.getHealth());
            }
            
            // Performs an attack if player has an allied mercenary
            for (MovingEntity ally : p1.getBribedAllies()) {
                if (p2.getHealth() > 0) {
                    healthModifier(p2, damageCalculation(ally, p2), ally.getHealth());
                }
            }

            // Then enemy attacks player (if the enemy is still alive)
            if (p2.getHealth() > 0) {
                healthModifier(p1, damageCalculation(p2, p1), p2.getHealth());
            }
            // If player is dead, check for The One Ring
            if (p1.getHealth() <= 0 && p1.hasItem("one_ring")) {
                p1.getInventory().useItem("one_ring");
            }
        }
        // If player wins the battle, random chance of spawning The One Ring and check enemy
        // for lootable armour
        if (p1.getHealth() > 0) {
            p1.lootBody(p2);
            spawnOneRing(p1);
            return p2;
        }
        else {
            return p1;
        }
    }

    /**
     * Checks for base dmg, swords, bows, anduril and midnight armour that can modify damage.
     * @param p1 (the entity's damage)
     * @param p2 (the entity receiving damage)
     */
    public double damageCalculation(MovingEntity p1, MovingEntity p2) {
        if (p1.getType().equals("player")) {
            Player plyr = (Player) p1;
            double dmg = plyr.getAttackDamage();
            Sword sword = (Sword) plyr.getInventory().getItem("sword");
            Bow bow = (Bow) plyr.getInventory().getItem("bow");
            Anduril anduril = (Anduril) plyr.getInventory().getItem("anduril");
            MidnightArmour mArmour = (MidnightArmour) plyr.getInventory().getItem("midnight_armour");
            if (sword != null) {
                dmg += sword.usedInCombat();
            }
            if (bow != null) {
                dmg += bow.usedInCombat();
            }
            if (anduril != null) {
                if (p2.getType().equals("assassin") || p2.getType().equals("hydra")) {
                    dmg += (3 * anduril.usedInCombat());
                }
                else {
                    dmg += anduril.usedInCombat();
                }
            }
            if (mArmour != null) {
                dmg += mArmour.getDamage();
            }
            return dmg;
        }
        else {
            return p1.getAttackDamage();
        }
    }

    public void healthModifier(MovingEntity p2, double dmg, double health) {
        double newHealth;
        if (p2.getType().equals("player")) {
            double multiplier = playerDamageMultiplier((Player) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 10);
        }
        else if (p2.getType().equals("mercenary")) {
            double multiplier = mercenaryDamageMultiplier((Mercenary) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.getType().equals("zombie_toast")) {
            double multiplier = zombieDamageMultiplier((ZombieToast) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.getType().equals("assassin")) {
            double multiplier = assassinDamageMultiplier((Assassin) p2);
            newHealth = p2.getHealth() - ((health * (dmg * multiplier)) / 5);
        }
        else if (p2.getType().equals("hydra")) {
            newHealth = hydraDamage((Hydra) p2, health, dmg);
        }
        else if (p2.isType("older_player")) {
            newHealth = 0;
            // Checks if the older player has midnight armour
            if (((Player) p2).getInventory().getItem("midnight_armour") != null) {
                return;
            }
        }
        else {
            newHealth = p2.getHealth() - ((health * dmg) / 5);
        }
        p2.setHealth(newHealth);
    }

    public double playerDamageMultiplier(Player player) {
        Shield shield = (Shield) player.getInventory().getItem("shield");
        Armour armour = (Armour) player.getInventory().getItem("armour");
        MidnightArmour mArmour = (MidnightArmour) player.getInventory().getItem("midnight_armour");
        double multiplier = 1;
        if (shield != null) {
            multiplier *= shield.usedInCombat();
        }
        if (armour != null) {
            multiplier *= armour.usedInCombat();
        }
        if (mArmour != null) {
            multiplier *= mArmour.usedInCombat();
        }
        return multiplier;
    }

    public double zombieDamageMultiplier(ZombieToast zombieToast) {
        Armour armour = zombieToast.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                zombieToast.setArmour(null);
            }
        }
        return multiplier;
    }

    public double mercenaryDamageMultiplier(Mercenary mercenary) {
        Armour armour = mercenary.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                mercenary.setArmour(null);
            }
        }
        return multiplier;
    }

    public double assassinDamageMultiplier(Assassin assassin) {
        Armour armour = assassin.getArmour();
        double multiplier = 1;
        if (armour != null) {
            multiplier *= armour.getReduceDamage();
            armour.reduceDurability();
            // Remove armour if it is broken.
            if (armour.getDurability() == 0) {
                assassin.setArmour(null);
            }
        }
        return multiplier;
    }

    public double hydraDamage(Hydra hydra, double dmgHealth, double dmg) {
        double newHealth;
        // Damage indicates that anduril is being used
        if (dmg >= 8) {
            newHealth = hydra.getHealth() - ((dmgHealth * dmg) / 5);
        }
        else {
            // Simulate 50/50 chance for hydra to gain health
            if (rand.nextBoolean()) {
                newHealth = hydra.getHealth() + dmg;
            }
            else {
                newHealth = hydra.getHealth() - ((dmgHealth * dmg) / 5);
            }
        }
        return newHealth;
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
