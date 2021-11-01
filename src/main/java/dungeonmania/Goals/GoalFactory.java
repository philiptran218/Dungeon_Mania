package dungeonmania.Goals;

public class GoalFactory {
	/**
	 * Creates the respective goal class given the goal type.
	 * 
	 * @param goalType - a string representing the type of goal to be made
	 * @return goal - a GoalInterface object
	 */
    public static GoalInterface getGoal(String goalType) {
		switch (goalType) {
			case "exit":
				return new ExitGoal();
			case "boulders":
				return new BouldersGoal();
			case "enemies":
				return new EnemiesGoal();
			case "treasure":
				return new TreasureGoal();
			case "AND":
				return new AndGoal();
			case "OR":
				return new OrGoal();
			default:
				return null;
		}
	}
}
