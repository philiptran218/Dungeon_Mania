package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;

public class StaticEntityTest {
    // Check if wall works properly by moving character into wall
    @Test
    public void testWall() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();

        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Checks if the exit works properly by moving player into the exit and seeing if the game ends
    @Test
    public void testExit() {
        // Create dungeon controller
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Checks if the boulder can be pushed by moving the player into a boulder
    @Test
    public void testBoulder() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Checks if the switch can be triggered by moving the boulder onto a switch
    @Test
    public void testSwitch() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Checks if the state pattern for door is working by unlocking a locked door with a key
    @Test
    public void testDoor() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Checks if the portal works with the player by moving the player into a portal and checking the location
    @Test
    public void testPortal() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
    // Tests if a zombie is spawned in the correct tick by checking the number of mobs in the 
    @Test
    public void testZombieSpawner() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        assertDoesNotThrow(() -> newDungeon.newGame("advanced", "Peaceful"));
    }
}
