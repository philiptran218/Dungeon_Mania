package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;


public class GameTestsDMC {
    // Helper:
    public String getUnix() {
        return "" + System.currentTimeMillis();
    }

    public void deleteSavedGames(String fileName) {
        File file = new File("src/main/resources/saved_games/" + fileName + ".json");
        file.delete();
    }

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


    // Test newGame:
    @Test
    public void testValidGameCreation() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Try all valid combinations of dungeon name to game mode:
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "peaceful"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "peaceful"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "peaceful"));

        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "standard"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "standard"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "standard"));

        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "hard"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "hard"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "hard"));
    }

    @Test
    public void testInvalidGameCreation() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        
        // Try invalid dungeon names and game modes
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("IncorrectDungeonName", "peaceful"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("advanced", "Incorrect game mode"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("Incorrect", "Incorrect"));
    }
    
    @Test
    public void testSaveGame() throws InterruptedException {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        // Create multiple games:
        newDungeon.newGame("file1", "peaceful");
        newDungeon.saveGame("test");
        Thread.sleep(3000);
        deleteSavedGames("test");
    }

    @Test
    public void testLoadGame() throws InterruptedException {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        assertDoesNotThrow(() -> {
            // Create multiple games:
            newDungeon.newGame("file1", "peaceful");
            newDungeon.saveGame("loadGame");
            newDungeon.loadGame("loadGame");
        });
    }

    // Test loadGame:
    @Test
    public void testInvalidLoadGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Attemp to load a game that does not exist:
        assertThrows(IllegalArgumentException.class, () -> newDungeon.loadGame("non existent game"));
    }

    // Coverage unused functions.
    @Test
    public void testAllGameFunctionValid() {
        assertDoesNotThrow(() -> {
            // Create dungeon controller
            DungeonManiaController newDungeon = new DungeonManiaController();
            newDungeon.getSkin();
            newDungeon.getLocalisation();
            Thread.sleep(3000);
            assertTrue(newDungeon.allGames().size() == 0);
        });
    }

    // Test uninteractable:
    @Test
    public void testInvalidInteractables() {
            // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse game = newDungeon.newGame("player_invalid_interactions", "peaceful");

        String zombieId = getEntityId(new Position(1, 2, 3), game);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.interact(zombieId));
    }

}
