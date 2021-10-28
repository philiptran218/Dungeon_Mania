package dungeonmania.gamemap;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Entity;

import dungeonmania.util.Position;

public class PeacefulState implements GameState{
    public int spawnZombie(int tickProgress, HashMap<Position, List<Entity>> listOfEntities, Position zombieSpawner) {
        return 0;
    }
}
