package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityTest {
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

    // ************************************************************ \\ 
    //                Test for Player Basic Movements
    // ************************************************************ \\ 

    // Test basic player movements
    @Test
    public void testBasicPlayerMovements() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse temp;
        // Create new game
        DungeonResponse createNew = controller.newGame("basic_player_move", "Peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1), createNew);

        // Moving the player:
        temp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(2, 1), playerId));

        temp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(3, 1), playerId));
        
        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(3, 2), playerId));

        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(3, 3), playerId));

        temp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(temp, new Position(2, 3), playerId));

        temp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(temp, new Position(1, 3), playerId));

        temp = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(temp, new Position(1, 2), playerId));

        // Back to where I started:
        temp = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));
    }

    // Test stationary movement:
    @Test
    public void testStationaryPlayerMovement() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse temp;
        // Create new game
        DungeonResponse createNew = controller.newGame("basic_player_move", "Peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1), createNew);

        // Stationary movement:
        temp = controller.tick(null, Direction.NONE);

        // Should be at the same position:
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));
    }

    // Test player moving into a wall:
    @Test
    public void testPlayerMovingIntoWall() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse temp;
        // Create new game
        DungeonResponse createNew = controller.newGame("player_wall_interaction", "Peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1), createNew);

        // Moving the player into walls should remain stationary:
        temp = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));

        temp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));

        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));

        temp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(temp, new Position(1, 1), playerId));
    }

    // ************************************************** \\ 
    //          Test for Player Exit Interaction
    // ************************************************** \\ 


    // Test for player boulder interaction:
    @Test
    public void testPlayerInteractionOneBoulder() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse temp;
        // Create new game
        DungeonResponse createNew = controller.newGame("player_wall_interaction", "Peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1), createNew);

        // Move player down one:
        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(1, 2), playerId));

        // Push the first boulder:
        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(1, 3), playerId));

        // Attempts to push the second boulder, but since player is not strong enough
        // will remian stationary at the same position.
        temp = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(temp, new Position(1, 3), playerId));
    }

    // ************************************************************ \\ 
    //               Test for Spider Basic Movements
    // ************************************************************ \\ 

}