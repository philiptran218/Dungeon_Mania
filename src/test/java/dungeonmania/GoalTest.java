package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GoalTest {
    /**
     * Test getting to exit
     */
    @Test
    public void ExitGoal() {
    }

    /**
     * Test destroying all enemies and spawners
     */
    @Test
    public void EnemiesGoal() {
    }

    /**
     * Test having a boulder on all floor switches
     */
    @Test
    public void BouldersGoal() {
    }

    /**
     * Test collecting all treasures
     */
    @Test
    public void TreasureGoal() {
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