package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


public class GameTestsDMC {
    // Helper:
    public String getUnix() {
        return "" + System.currentTimeMillis();
    }

    public void deleteSavedGames(String fileName) {
        File file = new File("src/main/resources/saved_games/" + fileName + ".json");
        file.delete();
    }

    // Test newGame:
    @Test
    public void testValidGameCreation() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Try all valid combinations of dungeon name to game mode:
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "Peaceful"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "Peaceful"));

        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Standard"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "Standard"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "Standard"));

        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Hard"));
        assertDoesNotThrow(() -> newDungeon.newGame("boulders", "Hard"));
        assertDoesNotThrow(() -> newDungeon.newGame("maze", "Hard"));
    }

    @Test
    public void testInvalidGameCreation() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        
        // Try invalid dungeon names and game modes
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("IncorrectDungeonName", "Peaceful"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("advanced", "Incorrect game mode"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("Incorrect", "Incorrect"));
    }
    
    @Test
    public void testSaveGame() throws InterruptedException {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        // Create multiple games:
        newDungeon.newGame("file1", "Peaceful");
        newDungeon.saveGame("test");
        Thread.sleep(3000);
        deleteSavedGames("test");
    }

    // Test loadGame:
    @Test
    public void testInvalidLoadGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Attemp to load a game that does not exist:
        assertThrows(IllegalArgumentException.class, () -> newDungeon.loadGame("non existent game"));
    }

}
