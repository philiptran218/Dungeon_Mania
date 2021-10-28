package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameMap;
import dungeonmania.util.Position;

public class TreasureGoal implements GoalInterface {
    
    private String goalName = "treasure";

    public boolean isGoalComplete(GameMap game) {
        Map<Position, List<Entity>> map = game.getMap();
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {
                if (entity.getType().equals("treasure")) {
                        return false;
                    }
            }
        }
        return true;
    }
    public void add(GoalInterface goal) {
    }

    @Override
    public String getGoalName() {
        return this.goalName;
    }
    @Override
    public List<GoalInterface> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }
}
