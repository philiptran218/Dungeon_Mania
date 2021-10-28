package dungeonmania.Goals;

import dungeonmania.gamemap.GameMap;

public class AndGoal extends CompositeGoal implements GoalInterface{
    private String goalName = "AND";
    
    public boolean isGoalComplete(GameMap game) {
        return true;
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }

    

}
