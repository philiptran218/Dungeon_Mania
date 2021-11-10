package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BattleTest {
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
    public String getEntityId(Position pos, DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()
                && entity.getType().equals(type)) {
                return entity.getId();
            }
        }
        return null;
    }

    // Tests for Battles:

    // Testing no weapon combat against a spider
    @Test
    public void basicBattle() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("onlySpiderAndPlayer", "hard");
        // Should kill the spider in combat
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("spider")));
    }

    // Tests using buildable weapons to kill a mercenary
    @Test
    public void testBuildableWeaponsMercenary() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("buildable_battle", "peaceful");

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.build("bow");
        newDungeon.build("shield");
        newDungeon.tick(null, Direction.RIGHT);
        // This movement should fight the mercenary and win
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    // Tests using shields in combat (should run down durability)
    @Test
    public void testShieldFight() {
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
        newDungeon.tick(null, Direction.RIGHT);
        // This movement should fight the mercenary (and use the player's shield)
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    // Tests that weapons are used in battle
    @Test
    public void testZeroDurability() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("mercenary_onslaught", "hard");
        DungeonResponse temp = null;

        for (int i = 0; i < 20; i++) {
            temp = newDungeon.tick(null, Direction.RIGHT);
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
        String MercId = getEntityId(new Position(4, 1, 3), tmp, "mercenary");
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.interact(MercId);
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue("".equals(tmp.getGoals()));
    }
}