package dungeonmania.Goals;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.gamemap.GameMap;

public abstract class CompositeGoal {
    List<GoalInterface> children = new ArrayList<GoalInterface>();

    public boolean isGoalComplete(GameMap game) {
        return true;
    }

    public void add(GoalInterface goal) {
        this.children.add(goal);
    }

    public List<GoalInterface> getChildren() {
        return children;
    }

    public void setChildren(List<GoalInterface> children) {
        this.children = children;
    }

    
}
