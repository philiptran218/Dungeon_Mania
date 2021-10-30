package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.gamemap.GameState;
import dungeonmania.gamemap.HardState;
import dungeonmania.gamemap.PeacefulState;
import dungeonmania.gamemap.StandardState;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    private int zombieId = 0;
    GameState state;
    GameState hardState;
    GameState standardState;
    GameState peacefulState;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
        hardState = new HardState();
        standardState = new StandardState();
        peacefulState = new PeacefulState();
    }
    /**
     * Spawns the zombie in 15 or 20 ticks depending on the game mode
     * @param zombieSpawner
     * @param listOfEntities
     */
    public void tick(Position zombieSpawner, Map<Position, List<Entity>> listOfEntities) {
        state.spawnZombie(tickProgress, listOfEntities, zombieSpawner, zombieId);
    }
    // Getters and setters
    public int getTickProgress() {
        return tickProgress;
    }
    public void setTickProgress(int tickProgress) {
        this.tickProgress = tickProgress;
    }
    public void destroy(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        map.get(new Position(pos.getX(), pos.getY(), 1)).clear();
    }

}
