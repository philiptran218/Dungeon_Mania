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

    public void deleteSavedGames() {
        DungeonManiaController d = new DungeonManiaController();
        File file;
        for (String s : d.allGames()) {
            file = new File("src/main/resources/saved_games/" + s + ".json");
            file.delete();
        }
        assertDoesNotThrow(() -> TimeUnit.SECONDS.sleep(1));
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
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("Incorrect Dungeon Name", "Peaceful"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("advanced", "Incorrect game mode"));
        assertThrows(IllegalArgumentException.class, () -> newDungeon.newGame("Incorrect", "Incorrect"));
    }

    // Test saveGames:
    @Test
    public void testSavingOneGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create a game and saving it:
        assertDoesNotThrow(() ->  {
            deleteSavedGames();
            newDungeon.newGame("advanced", "Peaceful");
            String gameName = getUnix();
            newDungeon.saveGame(getUnix());
            TimeUnit.SECONDS.sleep(1);
            assertTrue(newDungeon.allGames().contains(gameName));  
        }); 
        deleteSavedGames();
    }

    // Test saveGames:
    @Test
    public void testSavingMultipleGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create three game and saving it:
        assertDoesNotThrow(() ->  {
            deleteSavedGames();
            newDungeon.newGame("advanced", "Peaceful");
            String g1 = getUnix();
            newDungeon.saveGame(g1);
            String g2 = getUnix();
            newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(g2);
            String g3 = getUnix();
            newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(g3);
            TimeUnit.SECONDS.sleep(1);
            assertTrue(newDungeon.allGames().contains(g1));  
            assertTrue(newDungeon.allGames().contains(g2));  
            assertTrue(newDungeon.allGames().contains(g3));  
        });
        deleteSavedGames();
    }

    // Test loadGame:
    @Test
    public void testInvalidLoadGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Attemp to load a game that does not exist:
        assertThrows(IllegalArgumentException.class, () -> newDungeon.loadGame("non existent game"));
        deleteSavedGames();
    }

    @Test
    public void testLoadGame() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        // Create, save, and loading the game
        assertDoesNotThrow(() ->  {
            deleteSavedGames();
            newDungeon.newGame("advanced", "Peaceful");
            String savedName = getUnix();
            newDungeon.saveGame(savedName);
            newDungeon.loadGame(savedName);
        });
        deleteSavedGames();
    }

    @Test
    public void testGetAllGamesFunction() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create multiple games:
        assertDoesNotThrow(() ->  {
            deleteSavedGames();
            newDungeon.newGame("file1", "Peaceful");
            String savedName = getUnix();
            newDungeon.saveGame(savedName);
            assertEquals(1, newDungeon.allGames().size());
        });
        deleteSavedGames();
    }
}
