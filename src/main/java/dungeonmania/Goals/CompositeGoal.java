package dungeonmania.Goals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class CompositeGoal {
    List<GoalInterface> children = new ArrayList<GoalInterface>();

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
