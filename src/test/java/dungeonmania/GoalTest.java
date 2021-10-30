package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GoalTest {

    /**
     * Test getting to exit
     */
    @Test
    public void ExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleExit", "Peaceful");
        assertTrue(":exit".equals(tmp.getGoals()));
        for (int i = 0; i < 3; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test destroying all enemies and spawners
     */
    @Test
    public void EnemiesGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleEnemies", "Standard");
        assertTrue(":enemies".equals(tmp.getGoals()));
        for (int i = 0; i < 4; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test having a boulder on all floor switches
     */
    @Test
    public void BouldersGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleBoulder", "Standard");
        assertTrue(":boulders".equals(tmp.getGoals()));
        for (int i = 0; i < 2; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test collecting all treasures
     */
    @Test
    public void TreasureGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasure", "Peaceful");
        assertTrue(":treasure".equals(tmp.getGoals()));
        for (int i = 0; i < 3; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test destroying all enemies AND getting to exit
     * (Fail - Exit is achieved but Enemies is not achieved)
     */
    @Test
    public void TreasureAndExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasureAndExit", "Peaceful");
        assertTrue(":treasure AND :exit".equals(tmp.getGoals()));
        for (int i = 0; i < 3; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue(":exit".equals(tmp.getGoals()));
        for (int i = 0; i < 2; i++) {
            tmp = controller.tick(null, Direction.LEFT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test collecting all treasures OR reaching exit
     * (Pass - Reaching exit achieved)
     */
    @Test
    public void TreasureOrExitGoalPass() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasureOrExit", "Peaceful");
        assertTrue(":treasure OR :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test complex goal (Exit AND (Enemies OR Treasure))
     * (Pass - Exit and one of Enemies or Treasure is )
     */
    @Test
    public void ComplexGoalPass() {
    }

    /**
     * Test complex goal (Exit AND (Enemies OR Treasure))
     * (Fail - Only Enemies and Treasure are achieved)
     */
    @Test
    public void ComplexGoalFail() {
    }
}