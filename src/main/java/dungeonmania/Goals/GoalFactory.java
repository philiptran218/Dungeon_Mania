package dungeonmania.Goals;

public class GoalFactory {

    public static GoalInterface getGoal(String goalType) {
        GoalInterface goal = null;
        if (goalType.equals("exit")) {
			goal = new ExitGoal();
		} else if (goalType.equals("boulders")) {
			goal = new BouldersGoal();
		} else if (goalType.equals("enemies")) { 
			goal = new EnemiesGoal();
		} else if (goalType.equals("treasure")) {
			goal = new TreasureGoal();
		}
		return goal;
    }
}
