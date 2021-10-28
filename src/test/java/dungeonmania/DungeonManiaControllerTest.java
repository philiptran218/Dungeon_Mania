package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;


public class DungeonManiaControllerTest {
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
            newDungeon.newGame("player_wall_interaction", "Peaceful");
            newDungeon.saveGame(getUnix());
            TimeUnit.SECONDS.sleep(1);
        });
        assertTrue(newDungeon.allGames().size() == 1);
        deleteSavedGames();
    }
    
    @Test
    public void testSavingMultipleGames() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create multiple games:
        assertDoesNotThrow(() ->  {
            newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(getUnix());

            newDungeon.newGame("boulder", "Peaceful");
            newDungeon.saveGame(getUnix());

            newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(getUnix());
            TimeUnit.SECONDS.sleep(1);
        });
        assertEquals(newDungeon.allGames().size(), 3);
        deleteSavedGames();
    }

    // Test loadGame:
    @Test
    public void testInvalidLoadGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Attemp to load a game that does not exist:
        assertThrows(IllegalArgumentException.class, () -> newDungeon.loadGame("non existent game"));
    }

    @Test
    public void testLoadGame() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create, save, and loading the game
        assertDoesNotThrow(() ->  {
            DungeonResponse game = newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(game.getDungeonId());
            newDungeon.loadGame(game.getDungeonId());
        });
    }

    @Test
    public void testGetAllGamesFunctionForEmptyController() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Attempt to get the list of all games
        assertEquals(newDungeon.allGames().size(), 0);
    }

    @Test
    public void testGetAllGamesFunctionForMultipleSavedGames() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        // Create multiple games:
        assertDoesNotThrow(() ->  {
            DungeonResponse game1 = newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(game1.getDungeonId());

            DungeonResponse game2 = newDungeon.newGame("advanced", "Peaceful");
            newDungeon.saveGame(game2.getDungeonId());
        });

        // Check if there are indeed two games saved:
        assertEquals(newDungeon.allGames().size(), 2);
    }

}
