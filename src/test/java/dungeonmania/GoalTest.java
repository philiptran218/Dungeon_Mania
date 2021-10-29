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
    // Gets the id of entity on a position:
    public String getEntityId(Position pos, DungeonResponse response) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()) {
                return entity.getId();
            }
        }
        return null;
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

    /**
     * Test getting to exit
     */
    @Test
    public void ExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleExit", "Peaceful");
        for (int i = 0; i < 10; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
            System.out.println(tmp.getGoals());
        }
        assertFalse("".equals(tmp.getGoals()));
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

        for (int i = 0; i < 3; i++) {
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

        for (int i = 0; i < 2; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
            assertTrue("".equals(tmp.getGoals()));
        }
    }

    /**
     * Test collecting all treasures
     */
    @Test
    public void TreasureGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleExit", "Peaceful");

        for (int i = 0; i < 3; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test irrelevant goal. In this test, the goal will be Treasure, so if the
     * player reaches Exit, the game should not end.
     */
    @Test
    public void IrrelevantGoal() {
    }

    /**
     * Test destroying all enemies AND getting to exit 
     * (Pass - Both goals are complete)
     */
    @Test
    public void EnemiesAndExitGoalPass() {
    }

    /**
     * Test destroying all enemies AND getting to exit
     * (Fail - Exit is achieved but Enemies is not achieved)
     */
    @Test
    public void EnemiesAndExitGoalFail() {
    }

    /**
     * Test collecting all treasures OR having a boulder on all floor switches
     * (Pass - Only Treasure achieved)
     */
    @Test
    public void TreasureOrBoulderGoalPass1() {
    }

    /**
     * Test collecting all treasures OR having a boulder on all floor switches
     * (Pass - Only Boulder achieved)
     */
    @Test
    public void TreasureOrBoulderGoalPass2() {
    }
    
    /**
     * Test collecting all treasures OR having a boulder on all floor switches
     * (Pass - Treasure and Boulder achieved)
     */
    @Test
    public void TreasureOrBoulderGoalPass3() {
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