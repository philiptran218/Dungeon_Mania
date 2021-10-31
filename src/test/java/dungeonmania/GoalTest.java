package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

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
    public void testExitGoal() {
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
     * Test getting to exit with zombie in the way.
     */
    @Test
    public void testExitWithZombieGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("zombiesCoverExit", "Peaceful");
        System.out.println(tmp.getGoals());
        assertTrue(":exit".equals(tmp.getGoals()));
        for (int i = 0; i < 3; i++) {
            tmp = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test destroying a zombie
     */
    @Test
    public void testZombieGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleZombieToast", "Standard");
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test destroying a mercenary
     */
    @Test
    public void testKillMercenaryGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleMerc", "Standard");
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.LEFT);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test bribing a mercenary. Sometimes a spider may spawn which will cause
     * the test to fail.
     */
    @Test
    public void testBribeMercenaryGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleMercWithTreasure", "Standard");
        String MercId = getEntityId(new Position(4, 1, 3), tmp);
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.interact(MercId);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test destroying all enemies and spawners
     */
    @Test
    public void testEnemiesGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleEnemies", "Standard");
        String spawnerId = getEntityId(new Position(1, 1, 1), tmp);
        assertTrue(":enemies".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.interact(spawnerId);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test having a boulder on all floor switches
     */
    @Test
    public void testBouldersGoal() {
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
    public void testTreasureGoal() {
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
     * Test collecting treasure AND getting to exit
     * (Fail - Exit is achieved but Enemies is not achieved)
     */
    @Test
    public void testTreasureAndExitGoal() {
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
     * Test kill zombie AND collect treasure AND getting to exit
     * 
     */
    @Test
    public void testTreasureAndBoulderAndExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasureAndBoulderAndExit", "Standard");
        assertTrue(":treasure AND :boulders AND :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(":boulders AND :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.DOWN);
        assertTrue(":exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.UP);
        tmp = controller.tick(null, Direction.UP);
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
        assertTrue(":treasure AND :boulders OR :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(tmp.getGoals()));
    }

    /**
     * Test saving and loading
     * @throws InterruptedException
     */
    @Test
    public void SaveGoalPass() throws InterruptedException {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse tmp = controller.newGame("simpleTreasureAND(BouldersORExit)", "Peaceful");
        assertTrue(":treasure AND :boulders OR :exit".equals(tmp.getGoals()));
        tmp = controller.tick(null, Direction.RIGHT);
        tmp = controller.saveGame("simpleTreasureAND(BouldersORExit)");
        Thread.sleep(3000);
        tmp = controller.loadGame("simpleTreasureAND(BouldersORExit)");
        assertTrue(":boulders OR :exit".equals(tmp.getGoals()));
        File file = new File("src/main/resources/saved_games/simpleTreasureAND(BouldersORExit).json");
        file.delete();
    }
}