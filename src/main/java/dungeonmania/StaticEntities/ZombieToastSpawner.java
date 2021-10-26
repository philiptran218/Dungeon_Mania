package dungeonmania.StaticEntities;

import dungeonmania.GameState;
import dungeonmania.HardState;
import dungeonmania.StandardState;

public class ZombieToastSpawner extends StaticEntity {
    private int tickProgress = 0;
    GameState state;
    GameState hardState;
    GameState standardState;
    /**
     * Constructor for ZombieToastSpawner
     */
    public ZombieToastSpawner() {
        super.setCanStandOn(false);
        super.setType("ZombieToastSpawner");
        hardState = new HardState();
        standardState = new StandardState();
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
    public void tick() {
        tickProgress = state.spawnZombie(tickProgress);
    }
}
