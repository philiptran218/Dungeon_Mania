package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {
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
    // Testing ideas for actual CollectableEntities:

    // Test 1: check that the entity can be picked up by the player.
    // Condition: should appear in the player's inventory
    @Test
    public void testCollectablePickup() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "Peaceful");
        List<ItemResponse> inv;
        
        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("wood")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("arrow")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("treasure")));

        inv = newDungeon.tick(null, Direction.UP).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("key")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("sword")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("health_potion")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("invincibility_potion")));

        inv = newDungeon.tick(null, Direction.UP).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("invisibility_potion")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("bomb")));
    }

    // Test 2: check that armour/oneRing is obtained from enemy after battle
    // Condition: should appear in the player's inventory
    @Test
    public void testBattlePickups() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Standard"));

        // Create map, player and an enemy
        // Give the enemy armour as well as spawning theOneRing upon death
        // Perform combat until enemy is dead
        // After combat ends, check inventory for armour and ring
    }

    // Test 3: check that entity is successfully removed after being used
    // Condition: should not appear in player's inventory
    @Test
    public void testUsedCollectable() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "Peaceful");

        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);

        String healthId = getEntityId(new Position(2, 2, 2), temp);
        String invincibleId = getEntityId(new Position(1, 2, 2), temp);
        String invisibleId = getEntityId(new Position(1, 3, 2), temp);
        String bombId = getEntityId(new Position(2, 3, 2), temp);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);

        temp = newDungeon.tick(healthId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("health_potion")));
        temp = newDungeon.tick(invincibleId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("invincibility_potion")));
        temp = newDungeon.tick(invisibleId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("invisibility_potion")));
        temp = newDungeon.tick(bombId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("bomb")));

        // Create entity, player, map
        // Collect entity and then use it
        // Check that it is not in inventory anymore
        /*
        List<ItemResponse> inv = newDungeon.tick(PUT_ID_HERE, PUT_DIRECTION_HERE).getInventory();
        assertFalse(inv.stream().anyMatch(itm -> itm.getId().equals(PUT_ID_HERE)));
        */

    }
    @Test
    public void testUseBomb() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("bomb", "Peaceful");
        List<ItemResponse> inv;
        inv = newDungeon.tick(null, Direction.DOWN).getInventory();
        inv = newDungeon.tick("bomb", null).getInventory();
        assertFalse(inv.stream().anyMatch(itm -> itm.getType().equals("bomb")));
    }
    @Test
    public void testUseHealthPotion() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "Peaceful");
        List<ItemResponse> inv;
        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        inv = newDungeon.tick(null, Direction.UP).getInventory();
        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("health_potion")));
        inv = newDungeon.tick("health_potion", null).getInventory();
        assertFalse(inv.stream().anyMatch(itm -> itm.getType().equals("health_potion")));
    }
    // Test 4: test that each entity performs their intended functions
    // Condition: treasure/wood/arrow/key - for crafting
    //            key - opening correct door
    //            potions - provides special effects for the player
    //            bomb - explodes when placed on a switch
    //            sword - provides additional atk damage for player
    //            armour - halves damage received from enemy
    //            theOneRing - revives the player to full hp upon death
    @Test
    public void testCollectableFunctionality() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Standard"));

        // Have to set up situations for the entities to be used.
        // Best to separate these out.
    }
    @Test
    public void testBomb() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("bomb", "Peaceful");
        DungeonResponse temp;
        String switchId = getEntityId(new Position(3, 1, 0), createNew);
        String boulder = getEntityId(new Position(3, 1, 1), createNew);
        temp = newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick("bomb", null);
        assertFalse(isEntityOnTile(temp, new Position(3, 1, 1), boulder));
        assertFalse(isEntityOnTile(temp, new Position(3, 1, 1), boulder));
    }
    // SOME NOTES FOR COLLECTABLE ENTITIES:
    // - armour and theOneRing is added to inventory after defeating an enemy with it
    // - key disappears completely after being used (does not get melted/cannot be used to craft)
    
    

    // Some testing ideas for BuildableEntities (included in CollectableEntities):

    // Test 1: check that the entity can be built if the user has sufficient materials
    // Condition: check that the entity has been successfully added to inventory
    // Condition: check that materials have been removed from inventory
    @Test
    public void testSuccessfulBuildBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "Peaceful");
        List<ItemResponse> inv;
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        inv = newDungeon.build("bow").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("bow")));

        // Create player and utility entities
        // Get player to pick them up and craft bow/shield with it
        // Check that it is in inventory
    }
    @Test
    public void testSuccessfulBuildShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "Peaceful");
        List<ItemResponse> inv;
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        inv = newDungeon.build("shield").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("shield")));
    }
    // Test 2: check that entity is not built if user has insufficent materials
    // Condition: check that entity has not been added to inventory
    // Condition: check that materials are still in inventory
    @Test
    public void testUnsuccessfulBuildBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("bow"));
        // Create player
        // Try to craft with nothing in inventory
        // Should still be nothing there
    }
    @Test
    public void testUnsuccessfulBuildShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("shield"));
    }

    // Test 3: check that entities are performing correct functions in combat
    // Condition: Bow - attacks twice   Shield - reduces damage received
    @Test
    public void testBuildableFunctionality() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Standard"));

        // Create player, entities, enemies
        // Craft shield/bow
        // Fight enemy with shield/bow
        // Also good idea to test durability here as well

    }

    // Test 4 (assumption): check that a second bow/shield replaces the current bow/shield.
    // Condition: new bow/arrow should have full durability and only have 1 in inventory
    @Test 
    public void testBuildableLimit() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Standard"));

        // Create player, entities
        // Collect entities then craft shield/bow
        // Wear down durability
        // Create new shield/bow
        // Shield/bow in inventory should have full durability and limit to 1
    }

    // IDEAS FOR COMBAT:
    // Player has a method which returns the amount of damage they deal
    // Player has a method which takes in the amount of damage they receive and calculates
    // remaining health.
    // Enemies will have similar methods to calculate dealing/receiving battle damage.

    // Player vs enemy scenario:
    // enemy.recDamage(player.getDamage(), player.getHealth()); --> takes in player's attack and
    // current health to calculate enemy's current health after battle.
    // Similarly, player.recDamage(enemy.getDamage(), enemy.getHealth());
    // recDamage() also takes into account shield/armour to reduce damage received.
}
