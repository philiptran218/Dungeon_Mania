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
     * @param id
     * @param type
     * @param pos
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
    }
    /**
     * Ticks the zombie toast spawner
     * @param zombieSpawner
     * @param listOfEntities
     * @param state
     */
    public void tick(Position zombieSpawner, Map<Position, List<Entity>> listOfEntities, GameState state) {
        tickProgress = state.spawnZombie(tickProgress, listOfEntities, zombieSpawner);
    }
    /**
     * Destroys the zombie toast spawner
     * @param map
     */
    public void destroy(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        map.get(new Position(pos.getX(), pos.getY(), 1)).clear();
    }

}
