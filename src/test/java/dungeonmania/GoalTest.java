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
        DungeonResponse response = controller.newGame("simpleExit", "peaceful");
        assertTrue(":exit".equals(response.getGoals()));
        for (int i = 0; i < 3; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test getting to exit with zombie in the way.
     */
    @Test
    public void testExitWithZombieGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("zombiesCoverExit", "peaceful");
        assertTrue(":exit".equals(response.getGoals()));
        for (int i = 0; i < 3; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test destroying a zombie
     */
    @Test
    public void testZombieGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleZombieToast", "standard");
        assertTrue(":enemies".equals(response.getGoals()));
        response = controller.tick(null, Direction.RIGHT);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test destroying a mercenary
     */
    @Test
    public void testKillMercenaryGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleMerc", "standard");
        assertTrue(":enemies".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.tick(null, Direction.LEFT);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test bribing a mercenary.
     */
    @Test
    public void testBribeMercenaryGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleMercWithTreasure", "standard");
        String MercId = getEntityId(new Position(4, 1, 3), response);
        assertTrue(":enemies".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.interact(MercId);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test destroying all enemies and spawners
     */
    @Test
    public void testEnemiesGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleEnemies", "standard");
        String spawnerId = getEntityId(new Position(1, 1, 1), response);
        assertTrue(":enemies".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.interact(spawnerId);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test having a boulder on all floor switches
     */
    @Test
    public void testBouldersGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleBoulder", "standard");
        assertTrue(":boulders".equals(response.getGoals()));
        for (int i = 0; i < 2; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test collecting all treasures
     */
    @Test
    public void testTreasureGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleTreasure", "peaceful");
        assertTrue(":treasure".equals(response.getGoals()));
        for (int i = 0; i < 3; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("".equals(response.getGoals()));
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
        DungeonResponse response = controller.newGame("simpleTreasureAndExit", "peaceful");
        assertTrue("(:treasure AND :exit)".equals(response.getGoals()));
        for (int i = 0; i < 3; i++) {
            response = controller.tick(null, Direction.RIGHT);
        }
        assertTrue("(:exit)".equals(response.getGoals()));
        for (int i = 0; i < 2; i++) {
            response = controller.tick(null, Direction.LEFT);
        }
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test kill zombie AND collect treasure AND getting to exit
     */
    @Test
    public void testTreasureAndBouldersAndExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleTreasureAndBoulderAndExit", "standard");
        assertTrue("(:treasure AND :boulders AND :exit)".equals(response.getGoals()));
        response = controller.tick(null, Direction.RIGHT);
        assertTrue("(:boulders AND :exit)".equals(response.getGoals()));
        response = controller.tick(null, Direction.DOWN);
        assertTrue("(:exit)".equals(response.getGoals()));
        controller.tick(null, Direction.UP);
        response = controller.tick(null, Direction.UP);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test collecting all treasures OR reaching exit
     */
    @Test
    public void testTreasureOrExitGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleTreasureOrExit", "peaceful");
        assertTrue("(:treasure OR :exit)".equals(response.getGoals()));
        response = controller.tick(null, Direction.RIGHT);
        assertTrue("(:treasure OR :exit)".equals(response.getGoals()));
        response = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test collecting all treasures OR reaching exit OR boulders
     */
    @Test
    public void testTreasureOrExitorBouldersGoal() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleTreasureOrExitOrBoulders", "peaceful");
        assertTrue("(:treasure OR :exit OR :boulders)".equals(response.getGoals()));
        response = controller.tick(null, Direction.RIGHT);
        assertTrue("(:treasure OR :exit OR :boulders)".equals(response.getGoals()));
        response = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(response.getGoals()));
    }

    /**
     * Test complex goal (Treasure AND (Boulders OR Exit))
     * (Pass - Exit and one of Enemies or Treasure is )
     */
    @Test
    public void TreasureANDBouldersORExit() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleTreasureAND(BouldersORExit)", "peaceful");
        assertTrue("(:treasure AND (:boulders OR :exit))".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.tick(null, Direction.DOWN);
        assertTrue("".equals(response.getGoals()));
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
        DungeonResponse response = controller.newGame("simpleTreasureAND(BouldersORExit)", "peaceful");
        assertTrue("(:treasure AND (:boulders OR :exit))".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.saveGame("simpleTreasureAND(BouldersORExit)");
        Thread.sleep(3000);
        response = controller.loadGame("simpleTreasureAND(BouldersORExit)");
        assertTrue("((:boulders OR :exit))".equals(response.getGoals()));
        File file = new File("saved_games/simpleTreasureAND(BouldersORExit).json");
        file.delete();
    }
}