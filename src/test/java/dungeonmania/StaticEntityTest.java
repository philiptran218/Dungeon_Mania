package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;


public class StaticEntityTest {
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

    // Get the player:
    public String getPlayer(List<EntityResponse> eList) {
        for (EntityResponse e : eList) {
            if (e.getType().equals("player")) {
                return e.getId();
            }
        }
        return null;
    }
    
    // ***************************************************************************
    // Check if wall works properly by moving character into wall
    @Test
    public void testWall() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("advanced", "Peaceful");
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        // Move up and checks if wall stops the player 
        temp = newDungeon.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(temp, new Position(1, 1, 3), playerId));
    }
    // Checks if the boulder can be pushed by moving the player into a boulder
    @Test
    public void testBoulder() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("boulders", "Peaceful");
        String playerId = getEntityId(new Position(2, 2, 3), createNew);
        String boulder = getEntityId(new Position(3, 2, 1), createNew);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(3, 2, 3), playerId));
        assertTrue(isEntityOnTile(temp, new Position(4, 2, 1), boulder));
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(4, 2, 3), playerId));
        assertTrue(isEntityOnTile(temp, new Position(5, 2, 1), boulder));
    }
    // Checks if the door is working by unlocking a locked door with a key
    @Test
    public void testUnlockedDoor() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("door", "Peaceful");
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(3, 1, 3), playerId));
    }
    // Checks if the door is working by unlocking a locked door with a sun stone
    @Test
    public void testUnlockedDoorSunStone() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("door_sun_stone", "Peaceful");
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(4, 1, 3), playerId));
        // Check that sun stone is still in inventory
        assertTrue(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("sun_stone")));
    }
    // Checks if the door is actually locked by moving towards the door without a key
    @Test
    public void testLockedDoor() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("door", "Peaceful");
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        temp = newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(temp, new Position(3, 2, 3), playerId));
    }
    // Tests if a zombie is spawned in the correct tick by checking if the mob is spawned
    @Test
    public void testZombieSpawner() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("zombie_toast_spawn", "Hard");
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        String zombie = getEntityId(new Position(2, 2, 3), temp);
        assertTrue(isEntityOnTile(temp, new Position(2, 2, 3), zombie));
    }
    @Test
    public void testZombieSpawnerStandard() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("zombie_toast_spawn", "Standard");
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.UP);
        String zombie = getEntityId(new Position(2, 2, 3), temp);
        assertTrue(isEntityOnTile(temp, new Position(2, 2, 3), zombie));
    }
    // Tests if a player can destroy the zombie toast spawner
    @Test
    public void testInteractionZombieSpawner() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("zombie_toast_spawner", "Hard");
        temp = newDungeon.tick(null, Direction.RIGHT);
        String spawner = getEntityId(new Position(3, 1, 1), temp);
        temp = newDungeon.interact(spawner);
        assertFalse(isEntityOnTile(temp, new Position(3, 1, 1), spawner));
    }
    @Test
    public void testZombieSpawnerBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("zombie_toast_spawner_bow", "Standard");
        String spawner = getEntityId(new Position(3, 1, 1), temp);
        temp = newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.tick(null, Direction.UP);
        temp = newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);
        newDungeon.build("bow");
        temp = newDungeon.interact(spawner);
        assertFalse(isEntityOnTile(temp, new Position(3, 1, 1), spawner));
    }
    @Test
    public void testZombieSpawnerAnduril() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp = newDungeon.newGame("zombie_toast_spawner_anduril", "Standard");
        String spawner = getEntityId(new Position(3, 1, 1), temp);
        temp = newDungeon.tick(null, Direction.RIGHT);
        temp = newDungeon.interact(spawner);
        assertFalse(isEntityOnTile(temp, new Position(3, 1, 1), spawner));
    }
    // Tests if an exception is thrown if player is not in range of spawner
    @Test
    public void testNotInRangeException() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("zombie_toast_spawner", "Hard");
        String spawner = getEntityId(new Position(3, 1, 1), createNew);
        assertThrows(InvalidActionException.class, () -> newDungeon.interact(spawner));
    }
    // Tests if an exception is thrown if player tries to destroy spawner without a weapon
    @Test
    public void testNoWeaponException() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("zombie_toast_spawner", "Hard");
        String spawner = getEntityId(new Position(3, 1, 1), createNew);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.interact(spawner));
    }

    // Testing detonation of bomb:
    // Tests if an exception is thrown if player tries to destroy spawner without a weapon
    @Test
    public void testDetonateBomb() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("detonating_bomb", "Peaceful");
        DungeonResponse tmp;
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        String boulderId = getEntityId(new Position(3, 2, 1), createNew);
        String bombId = getEntityId(new Position(3, 4, 2), createNew);
        String switchId = getEntityId(new Position(3, 3, 2), createNew);

        // Explode the bomb:
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        tmp = newDungeon.tick(null, Direction.DOWN);
        
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 1), boulderId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 4, 2), bombId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 2), switchId));
        assertTrue(isEntityOnTile(tmp, new Position(3, 2, 3), playerId));
    }

    // Testing detonation of bomb after picking up then placing it
    // down.
    @Test
    public void testDetonateBombAfterPickUp() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("detonating_bomb", "Peaceful");
        DungeonResponse tmp;
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        String boulderId = getEntityId(new Position(3, 2, 1), createNew);
        String bombId = getEntityId(new Position(3, 4, 2), createNew);
        String switchId = getEntityId(new Position(3, 3, 0), createNew);

        // Get bomb:
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);

        // Use the bomb:
        tmp = newDungeon.tick(bombId, Direction.NONE);
        assertTrue(isEntityOnTile(tmp, new Position(3, 4, 2), bombId));

        // Push boulder onto the swtich:
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.RIGHT);
        tmp = newDungeon.tick(null, Direction.DOWN);
        
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 1), boulderId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 4, 2), bombId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 2), switchId));
        assertTrue(isEntityOnTile(tmp, new Position(3, 2, 3), playerId));
    }

    // Testing detonation of bomb after picking up then placing it
    // down.
    @Test
    public void testBombKillMobs() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("detonating_bomb", "Peaceful");
        DungeonResponse tmp;
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        String boulderId = getEntityId(new Position(3, 2, 1), createNew);
        String bombId = getEntityId(new Position(3, 4, 2), createNew);
        String switchId = getEntityId(new Position(3, 3, 0), createNew);
        String merceId = getEntityId(new Position(4, 6, 3), createNew);
        String wallId = getEntityId(new Position(4, 3, 1), createNew);

        // Explode the bomb:
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        tmp = newDungeon.tick(null, Direction.DOWN);
        
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 1), boulderId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 4, 2), bombId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 2), switchId));
        assertFalse(isEntityOnTile(tmp, new Position(4, 6, 3), merceId));
        assertFalse(isEntityOnTile(tmp, new Position(4, 3, 1), wallId));
        assertTrue(isEntityOnTile(tmp, new Position(3, 2, 3), playerId));
    }

    // Testing detonation of bomb radius
    @Test
    public void testDetonationRadius() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse createNew = newDungeon.newGame("detonating_bomb", "Peaceful");
        DungeonResponse tmp;
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        String boulderId = getEntityId(new Position(3, 2, 1), createNew);
        String bombId = getEntityId(new Position(3, 4, 2), createNew);
        String switchId = getEntityId(new Position(3, 3, 0), createNew);
        String merceId = getEntityId(new Position(4, 6, 3), createNew);
        String wallId = getEntityId(new Position(4, 3, 1), createNew);

        // Pick up bomb
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);

        // Place bomb:
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(bombId, Direction.NONE);

        // Explode the bomb:
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.RIGHT);
        tmp = newDungeon.tick(null, Direction.DOWN);

        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 1), boulderId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 4, 2), bombId));
        assertFalse(isEntityOnTile(tmp, new Position(3, 3, 2), switchId));
        assertFalse(isEntityOnTile(tmp, new Position(4, 6, 3), merceId));
        assertTrue(isEntityOnTile(tmp, new Position(4, 3, 1), wallId));
        assertTrue(isEntityOnTile(tmp, new Position(3, 2, 3), playerId));
    }

    // Test for saving portals of different colours
    @Test
    public void testColourPortals() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp;
        // Create new game
        DungeonResponse r = controller.newGame("portal_test_big", "Peaceful");
        String id = getPlayer(r.getEntities());

        // Movements around the portal:
        controller.tick(null, Direction.DOWN);
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(6, 2), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(tmp, new Position(1, 4), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(6, 6), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(tmp, new Position(1, 8), id));
    }

    // ********************************************************************* \\
    //                            Test SwampTile                             \\
    // ********************************************************************* \\

    // Test normal movement of the player
    @Test
    public void testBasicPlayerMovementThroughTile() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp = null;
        // Create new game
        DungeonResponse r = controller.newGame("swamp_tile_test", "Peaceful");
        String id = getPlayer(r.getEntities());
        
        // Move above tile
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        for (int i = 0; i < 7; i++) {
            tmp = controller.tick(null, Direction.DOWN);
        }
        // Check if the player is at the correct position
        assertTrue(isEntityOnTile(tmp, new Position(3, 5), id));
    }

    // Test mobs that cross the swamp
    @Test
    public void testMobMovementOnSwampTile() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp = null;
        // Create new game
        DungeonResponse r = controller.newGame("swamp_tile_test", "Peaceful");
        String id = getPlayer(r.getEntities());
        String merc = getEntityId(new Position(4, 4, 3) ,r);
        String spider = getEntityId(new Position(3, 5, 3) ,r);
        
        // Move the entity:
        for (int i = 0; i < 5; i++) {
            tmp = controller.tick(null, Direction.LEFT);
        }
        // Check if the player is at the correct position
        assertTrue(isEntityOnTile(tmp, new Position(1, 1), id));
        assertTrue(isEntityOnTile(tmp, new Position(2, 2), merc));
        assertTrue(isEntityOnTile(tmp, new Position(4, 5), spider));
    }

    // Test saving and loading entities on the swamp tile
    @Test
    public void testSwampTileLoading() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp = null;
        // Create new game
        DungeonResponse r = controller.newGame("swamp_tile_test", "Peaceful");
        String merc = getEntityId(new Position(4, 4, 3) ,r);
        String spider = getEntityId(new Position(3, 5, 3) ,r);
        
        // Move the mobs:
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);

        controller.saveGame("swamp_tile_test");
        tmp = controller.loadGame("swamp_tile_test");

        // New id
        merc = getEntityId(new Position(3, 3, 3) ,tmp);
        spider = getEntityId(new Position(3, 4, 3) ,tmp);

        // Move again, the entity should not move:
        tmp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(tmp, new Position(3, 3), merc));
        assertTrue(isEntityOnTile(tmp, new Position(3, 4), spider));
    }

    // Test boulder pushing on swampTile
    @Test
    public void testSwampTilePushingBoulder() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp = null;
        // Create new game
        DungeonResponse r = controller.newGame("swamp_tile_boulder", "Peaceful");
        String id = getEntityId(new Position(1, 1, 3) ,r);
        String boulder = getEntityId(new Position(2, 1, 1) ,r);
        
        // Move the mobs:
        controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.RIGHT);

        // Boulder should remain at the same tile
        assertTrue(isEntityOnTile(tmp, new Position(2, 1), id));
        assertTrue(isEntityOnTile(tmp, new Position(3, 1), boulder));

        // The boulder now should move upon being moved once more
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(3, 1), id));
        assertTrue(isEntityOnTile(tmp, new Position(4, 1), boulder));
    }

    // Test boulder pushing on swampTile saving and loading
    @Test
    public void testSwampTileBoulderSaveAndLoad() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp = null;
        // Create new game
        DungeonResponse r = controller.newGame("swamp_tile_boulder", "Peaceful");
        String boulder = getEntityId(new Position(2, 1, 1) ,r);
        
        // Move the mobs:
        controller.tick(null, Direction.RIGHT);

        // Save and load
        controller.saveGame("swamp_tile_boulder_test");
        tmp = controller.loadGame("swamp_tile_boulder_test");

        // Get new id:
        boulder = getEntityId(new Position(3, 1, 1) ,tmp);

        // Push the boulder
        controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(4, 1), boulder));
        //////
        SwampTile s = new SwampTile("asdasd", "swamp_tile", new Position(1, 2), 2);
        s.getFactor();
        s.setFactor(2);
        s.getMap();
    }

}
