package dungeonmania.Goals;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.util.Position;

public class ExitGoal implements GoalInterface {
    
    private String goalName = "exit";

    @Override
    public boolean isGoalComplete(Map<Position, List<Entity>> map) {
        for(List<Entity> entities : map.values()) {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    System.out.println("PLAYER");
                    System.out.println(entity.getPos().getX() + " " + entity.getPos().getY() + " " + entity.getPos().getLayer());
                }
                
                if (entity instanceof Exit 
                    && ((Exit) entity).isUnderPlayer(map)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
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
