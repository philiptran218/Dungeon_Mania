package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    // Checks if the portal works with the player by moving the player into a portal and checking the location
    @Test
    public void testPortal() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("portals", "Peaceful");
        String playerId = getEntityId(new Position(1, 1, 3), createNew);
        temp = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(6, 1, 3), playerId));
    }
    // Tests if a zombie is spawned in the correct tick by checking if the mob is spawned
    @Test
    public void testZombieSpawner() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse temp;
        DungeonResponse createNew = newDungeon.newGame("zombie_toast_spawner", "Hard");
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
        String zombie = getEntityId(new Position(3, 2, 3), createNew);
        assertTrue(isEntityOnTile(temp, new Position(3, 2, 3), zombie));
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
}
