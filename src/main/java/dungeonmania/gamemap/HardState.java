package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HardState implements GameState {
    public int spawnZombie(int tickProgress, HashMap<Position, List<Entity>> listOfEntities, Position zombieSpawner) {
        if (tickProgress == 15) {            
            List <Direction> directions = new ArrayList<Direction>();
            directions.add(Direction.UP);
            directions.add(Direction.RIGHT);
            directions.add(Direction.DOWN);
            directions.add(Direction.LEFT);
            for (Direction dir : directions) {
                Position checkOpenPosition = zombieSpawner.translateBy(dir);
                List <Entity> entitiesOnPosition = listOfEntities.get(checkOpenPosition);
                if (entitiesOnPosition.get(0) == null || entitiesOnPosition.get(0) instanceof CollectableEntity || 
                    entitiesOnPosition.get(0) instanceof Exit || entitiesOnPosition.get(0) instanceof Portal) {
                    ZombieToast newZombie = new ZombieToast();
                    entitiesOnPosition.add(newZombie);
                    break;
                }
                else if (entitiesOnPosition.get(0) instanceof FloorSwitch) {
                    if (entitiesOnPosition.get(1) == null) {
                        ZombieToast newZombie = new ZombieToast();
                        entitiesOnPosition.add(newZombie);
                        break;
                    }
                }
                else if (entitiesOnPosition.get(0) instanceof Door) {
                    Door checkLocked = (Door) entitiesOnPosition.get(0);
                    if (checkLocked.getCanStandOn() == true) {
                        Entity newZombie = new ZombieToast();
                        entitiesOnPosition.add(newZombie);
                        break;
                    }
                }
            }
            tickProgress = 1;
        }
        else {
            tickProgress++;
        }
        return tickProgress;
    }
}
