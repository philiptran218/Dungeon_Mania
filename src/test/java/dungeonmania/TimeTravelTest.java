package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TimeTravelTest {
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
    public String getEntityId(Position pos, DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getType().equals(type)) {
                return entity.getId();
            }
        }
        return null;
    }

    // Gets the id of entity on a position:
    public boolean entityTypeExists(DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    //**************************************************************************************************\\

    @Test
    public void testOneTickRewind() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response  = newDungeon.newGame("time_turner_test", "peaceful");
        String playerId = getEntityId(new Position(1, 1), response, "player");
        response = newDungeon.tick(null, Direction.RIGHT);
        // Check player position
        assertTrue(isEntityOnTile(response, new Position(2, 1), playerId));
        // Move to the right and pick up timer turner
        response = newDungeon.rewind(1);
        // Check if the player is back at 1, 1 now:
        assertTrue(getEntityId(new Position(1, 1), response, "player") != null);

        // Move down:
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(getEntityId(new Position(1, 2), response, "player") != null);
        assertTrue(getEntityId(new Position(2, 1), response, "older_player") != null);

        // Older self will disappear after catching up to the current tick
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(entityTypeExists(response, "older_player") == false);
    }

    @Test
    public void testFiveeTickRewind() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response  = newDungeon.newGame("time_turner_test", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);

        // Rewind:
        response = newDungeon.rewind(5);
        String oldPlayer = getEntityId(new Position(2, 1), response, "older_player");
        // Check if the older player will move in the same direction as previously
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(3, 1), oldPlayer));
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(4, 1), oldPlayer));
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(5, 1), oldPlayer));
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(6, 1), oldPlayer));
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(6, 2), oldPlayer));

        // Older player should now disappear:
        response = newDungeon.tick(null, Direction.DOWN);
        assertTrue(entityTypeExists(response, "older_player") == false);
    }
    
    @Test
    public void killingOldSelf() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response  = newDungeon.newGame("time_turner_test", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);

        // Rewind:
        response = newDungeon.rewind(5);
        assertTrue(entityTypeExists(response, "older_player") == true);
        // Older player should now disappear:
        newDungeon.tick(null, Direction.LEFT);
        response = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(entityTypeExists(response, "older_player") == false);
    }

    @Test
    public void timeTravellingPortal() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response  = newDungeon.newGame("time_turner_test", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);
        newDungeon.tick(null, Direction.UP);

        // Rewind:
        response = newDungeon.rewind(5);
        assertTrue(entityTypeExists(response, "older_player") == true);
        // Older player should now disappear:
        newDungeon.tick(null, Direction.LEFT);
        response = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(entityTypeExists(response, "older_player") == false);
    }

}
