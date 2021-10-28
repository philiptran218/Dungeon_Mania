package dungeonmania.gamemap;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Entity;

import dungeonmania.util.Position;

public class StandardState implements GameState{
    public int spawnZombie(int tickProgress, HashMap<Position, List<Entity>> listOfEntities, Position zombieSpawner) {
        if (tickProgress == 20) {
            tickProgress = 1;
        }
        else {
            tickProgress++;
        }
        return tickProgress;
    }

}
