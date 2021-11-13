package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonGeneratorTest {
    // Helper
    // Gets the id of entity on a position:
    public String getEntityId(Position pos, DungeonResponse response) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()) {
                return entity.getId();
            }
        }
        return null;
    }

    @Test
    public void testInvalidDungeonGeneration() {
        // Create dungeon controller
        DungeonManiaController controller = new DungeonManiaController();
        
        // Try invalid dungeon game modes
        assertThrows(IllegalArgumentException.class, () -> controller.generateDungeon(3, 3, 4, 3, "asdfa"));
    }

    @Test
    public void testValidDungeonGeneration() {
        DungeonManiaController controller = new DungeonManiaController();
        assertDoesNotThrow(() -> controller.generateDungeon(3, 3, 4, 3, "peaceful"));
    }

    // Check that the player has spawned at the starting position
    @Test
    public void testDungeonGenerationPlayerSpawn() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("random", "peaceful");
        String playerId = getEntityId(new Position(3, 3, 3), response);
        assertNotEquals(playerId, null);
    }

    // Check that the exit tile exists at the end position
    @Test
    public void testDungeonGenerationExitExists() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("random", "peaceful");
        String exitId = getEntityId(new Position(4, 3, 0), response);
        assertNotEquals(exitId, null);
    }

    @Test
    public void testDungeonGenerationExitGoalCompletion() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("random", "peaceful");
        String playerId = getEntityId(new Position(3, 3, 3), response);
        assertTrue(":exit".equals(response.getGoals()));


    }


}
