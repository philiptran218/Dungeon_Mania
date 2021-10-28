package dungeonmania.Goals;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameMap;
import dungeonmania.util.Position;

import java.util.Map;

public class EnemiesGoal implements GoalInterface {
    
    private String goalName = "enemies";

    @Override
    public boolean isGoalComplete(GameMap game) {
        Map<Position, List<Entity>> map = game.getMap();
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {
                if (entity.getType().equals("zombie_toast")
                    || entity.getType().equals("zombie_toast_spawner")) {
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
        return null;
    }
}
