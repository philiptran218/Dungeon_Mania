package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.Battles.Battle;
import dungeonmania.Battles.BattleState;
import dungeonmania.Battles.NormalState;
import dungeonmania.CollectableEntities.Bow;
import dungeonmania.CollectableEntities.Shield;
import dungeonmania.CollectableEntities.Sword;
import dungeonmania.CollectableEntities.TheOneRing;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.Spider;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BattleTest {

    // ********************************************************************************************\\
    //                                       Helper Methods                                        \\
    // ********************************************************************************************\\

    // Test helper: Checks if entity on a given position.
    public boolean isEntityOnTile(DungeonResponse response, Position pos, String id) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getId() == id) {
                return entity.getPosition().equals(pos);
            }
        }
        return false;
    }

    // Gets the id of entity on a position:
    public String getEntityId(Position pos, DungeonResponse response) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()) {
                return entity.getId();
            }
        }
        return null;
    }

    // Gets the id of entity on a position:
    public EntityResponse getEntity(Position pos, DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()
                && entity.getType().equals(type)) {
                return entity;
            }
        }
        return null;
    }

    // ********************************************************************************************\\
    //                                       Black Box                                             \\
    //                                      Battle Tests                                           \\
    // ********************************************************************************************\\

    // Testing no weapon combat against a spider
    @Test
    public void testPlayerBattleSpiderNoWeapons() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("onlySpiderAndPlayer", "hard");
        // Should kill the spider in combat
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("spider")));
    }

    // Tests using buildable weapons to kill a mercenary
    @Test
    public void testPlayerBattleMercenaryWithBowAndShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        // Start the game
        newDungeon.newGame("buildable_battle", "peaceful");
        // Tick the game, in this case moving the player to the right
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.build("bow");
        newDungeon.build("shield");
        newDungeon.tick(null, Direction.DOWN);
        // This movement should fight the mercenary and win
        DungeonResponse response = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(response.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    // Tests using shields in combat (should run down durability)
    @Test
    public void testPlayerBattleMercenaryWithShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("buildable_battle", "hard");

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.build("shield");
        newDungeon.tick(null, Direction.DOWN);
        // This movement should fight the mercenary (and use the player's shield)
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    // Tests that weapons are used in battle
    @Test
    public void testPlayerBattleMercenaryWithWeapons() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("mercenary_onslaught", "hard");
        DungeonResponse temp = null;

        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.RIGHT);
        }
        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.LEFT);
        }
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        // Player should still be alive
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }

    // Tests fighting against zombies
    @Test 
    public void testFightZombies() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("zombie_battle", "hard");

        for (int i = 0; i < 4; i++) {
            newDungeon.tick(null, Direction.RIGHT);
            newDungeon.tick(null, Direction.DOWN);
            newDungeon.tick(null, Direction.LEFT);
            newDungeon.tick(null, Direction.UP);
        }
        // By now, all zombies should be dead
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast")));
    }

    // Tests fighting while player is invisible (nothing should happen)
    @Test 
    public void testInvisibilityFighting() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("invisibility_battle", "hard");

        String invisId = getEntityId(new Position(2, 1, 2), temp);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(temp.getInventory().stream().anyMatch(e -> e.getType().equals("invisibility_potion")));
        temp = newDungeon.tick(invisId, null);
        assertFalse(temp.getInventory().stream().anyMatch(e -> e.getType().equals("invisibility_potion")));

        // Once invisible potion is used, player should be able to walk through enemies
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);

        // Assert that enemies are still alive
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("zombie_toast")));
    }

    // Tests using invincible potion and then battling.
    @Test
    public void testInvincibleBattle() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("invincibleBattle", "standard");
        String invincibleId = getEntityId(new Position(1, 0, 2), temp);
        String zombieId = getEntityId(new Position(2, 1, 3), temp);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(invincibleId, null);
        newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(isEntityOnTile(temp, new Position(2, 1, 3), zombieId));
    }

    @Test
    public void testBowCombat() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("onslaught_bow", "hard");
        DungeonResponse temp = null;

        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.RIGHT);
        }
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        // Player should still be alive
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }

    @Test 
    public void testArmourCombat() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("onslaught_armour", "hard");
        DungeonResponse temp = null;

        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.RIGHT);
        }
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        // Player should still be alive
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }
    @Test 
    public void testArmourCombatInvincible() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("onslaught_armour_invincible", "hard");
        String potionId = getEntityId(new Position(2,1,2), temp);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(potionId, null);
        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.RIGHT);
        }
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        // Player should still be alive
        assertTrue(temp.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }

    @Test
    public void testBattleWithMerc() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("battleWithAllyMerc", "standard");
        String MercId = getEntity(new Position(4, 1, 3), tmp, "mercenary").getId();
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.interact(MercId);
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue("".equals(tmp.getGoals()));
    }

    // Tests battle against a hydra using anduril. Should die within several ticks since it
    // cannot heal against anduril attacks.
    @Test
    public void testAndurilAgainstHydra() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("hydra_anduril", "Hard");
        // TODO: Add more directions since hydra movement is random
        DungeonResponse info = newDungeon.tick(null, Direction.RIGHT);
        info = newDungeon.tick(null, Direction.RIGHT);
        // Should now fight against the hydra and win after x rounds since there is also
        // triple damage to bosses
        assertFalse(info.getEntities().stream().anyMatch(e -> e.getType().equals("hydra")));
        // Player should still be alive
        assertTrue(info.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }

    // Tests battle against an assassin using anduril. Should die within several ticks since it deals
    // triple damage to bosses.
    @Test
    public void testAndurilAgainstAssassin() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("assassin_anduril", "Hard");
    
        DungeonResponse info = newDungeon.tick(null, Direction.RIGHT);
        info = newDungeon.tick(null, Direction.RIGHT);
        // Should now fight against the assassin and win after x rounds, taking triple damage
        // into account
        assertFalse(info.getEntities().stream().anyMatch(e -> e.getType().equals("assassin")));
        assertTrue(info.getEntities().stream().anyMatch(e -> e.getType().equals("player")));
    }

    // Tests anduril against a non-boss enemy. Should just deal normal damage.
    @Test
    public void testAndurilAgainstMercenary() {
        // Leave as stub for now, will complete once finished...
    }

    // ********************************************************************************************\\
    //                                       White Box                                             \\
    //                                      Battle Tests                                           \\
    // ********************************************************************************************\\

    @Test
    public void testUsingNoWeaponsInBattle() {
        Player player = new Player(null, "player", null, 20, 20, 2);
        Mercenary merc = new Mercenary(null, "mercenary", null);
        NormalState battle = new NormalState(new Battle("standard"));
        battle.fight(player, merc);
        // Player should beat the mercenary in a 1v1 as the player has more health
        assertTrue(merc.getHealth() <= 0);
    }

    @Test
    public void testUsingSwordInBattle() {
        Player player = new Player(null, "player", null, 20, 20, 6.5);
        Sword sword = new Sword(null, "sword", null);
        player.getInventory().put(sword, player);
        Mercenary merc = new Mercenary(null, "mercenary", null);
        NormalState battle = new NormalState(new Battle("standard"));
        battle.fight(player, merc);
        // The player should have just enough damage to one shot the mercenary
        // with a sword and thus should not lose any health
        assertEquals(player.getHealth(),20);
        // Asserting that the sword only loses 1 point in durability to ensure that
        // the player only attacks once.
        assertEquals(sword.getDurability(), 9);
    }

    @Test
    public void testUsingBowInBattle() {
        Player player = new Player(null, "player", null, 20, 20, 2);
        Bow bow = new Bow (null, "bow", null);
        player.getInventory().put(bow, player);
        Mercenary merc = new Mercenary(null, "mercenary", null);
        NormalState battle = new NormalState(new Battle("standard"));
        battle.fight(player, merc);
        // The player should have just enough damage to one shot the mercenary 
        // (does not matter if the mercenary has armour or not) with a bow and 
        // thus should not lose any health.
        assertEquals(player.getHealth(),20);
        // Asserting that the bow only lose 2 point in durability to ensure that
        // the player only attacks twice (the bow's ability).
        assertEquals(bow.getDurability(), 8);
    }

    @Test
    public void testUsingShieldInBattle() {
        Player player = new Player(null, "player", null, 20, 20, 0.7);
        Shield shield = new Shield (null, "shield", null);
        player.getInventory().put(shield, player);
        Spider spider = new Spider(null, "spider", new Position(0, 0));
        NormalState battle = new NormalState(new Battle("standard"));
        battle.fight(player, spider);
        // After the player's first attack, the spider should be on 2.2 health
        // and attack the player and the shield should reduce the spiders damage.
        // The players next attack will kill the spider so the spider will only attack
        // once.
        assertEquals(player.getHealth(), 19.868);
        // Asserting that the bow only lose 2 point in durability to ensure that
        // the player only attacks twice (the bow's ability).
        assertEquals(shield.getDurability(), 9);
    }

    @Test
    public void testUsingOneRing() {
        Player player = new Player(null, "player", null, 2, 2, 37.5);
        TheOneRing ring = new TheOneRing(null, "one_ring", null);
        player.getInventory().put(ring, player);
        Mercenary mercenary = new Mercenary(null, "mercenary", null);
        NormalState battle = new NormalState(new Battle("standard"));
        battle.fight(player, mercenary);
        // After the player's first attack, the mercenary should be on 10 health
        // and attack the player and deal 2 points of damage, killing the player.
        // However, the player has the one-ring which will activate and restore the
        // player to full health. The player will then attack the mercenary and kill
        // them whether or not the mercenry has armour and the battle ends.
        assertEquals(player.getHealth(), 2);
    }
}