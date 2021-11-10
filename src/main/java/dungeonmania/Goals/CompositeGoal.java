package dungeonmania.Goals;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeGoal implements Goal{
    List<Goal> children = new ArrayList<Goal>();
    
    public void add(Goal goal) {
        this.children.add(goal);
    }

    public List<Goal> getChildren() {
        return children;
    }

    public void setChildren(List<Goal> children) {
        this.children = children;
    }

    
}
