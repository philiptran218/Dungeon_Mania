package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameState;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
    }
    /**
     * Spawns the zombie in 15 or 20 ticks depending on the game mode
     */
    public void tick(Position zombieSpawner, Map<Position, List<Entity>> listOfEntities, GameState state) {
        tickProgress = state.spawnZombie(tickProgress, listOfEntities, zombieSpawner);
    }

    public void destroy(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        map.get(new Position(pos.getX(), pos.getY(), 1)).clear();
    }

}
