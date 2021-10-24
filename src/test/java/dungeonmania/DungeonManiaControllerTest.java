package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dungeonmania.exceptions.InvalidActionException;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;

public class DungeonManiaControllerTest {
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

    @Test
    public void test

}
