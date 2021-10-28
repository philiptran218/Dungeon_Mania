package dungeonmania.StaticEntities;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameState;
import dungeonmania.gamemap.HardState;
import dungeonmania.gamemap.PeacefulState;
import dungeonmania.gamemap.StandardState;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    GameState state;
    GameState hardState;
    GameState standardState;
    GameState peacefulState;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
        hardState = new HardState();
        standardState = new StandardState();
        peacefulState = new PeacefulState();
    }
    /**
     * Getter for tickProgress
     * @return the progress of the tick
     */
    public int getTickProgress() {
        return tickProgress;
    }
    /**
     * Setter for tickProgress
     * @param tickProgress
     */
    public void setTickProgress(int tickProgress) {
        this.tickProgress = tickProgress;
    }
    /**
     * Spawns the zombie in 15 or 20 ticks depending on the game mode
     */
    public void tick(Position zombieSpawner, HashMap<Position, List<Entity>> listOfEntities) {
        tickProgress = state.spawnZombie(tickProgress, listOfEntities, zombieSpawner);
    }
}
