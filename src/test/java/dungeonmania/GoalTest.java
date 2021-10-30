package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String spawnerId = getEntityId(new Position(4, 1, 1), tmp);
        assertTrue(":enemies".equals(tmp.getGoals()));
        for (int i = 0; i < 4; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        tmp = controller.interact(spawnerId);
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
        assertTrue(":treasure OR :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test complex goal (Treasure AND (Boulders OR Exit))
     * (Pass - Exit and one of Enemies or Treasure is )
     */
    @Test
    public void ComplexGoalPass() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasureAND(BouldersORExit)", "Peaceful");
        System.out.println(tmp.getGoals());
        assertTrue(":treasure AND :boulders OR :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(tmp.getGoals()));
    }
}