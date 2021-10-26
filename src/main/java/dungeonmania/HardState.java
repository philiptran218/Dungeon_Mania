package dungeonmania;

public class HardState implements GameState {
    public int spawnZombie(int tickProgress) {
        if (tickProgress == 15) {
            // SPAWN ZOMBIE
            tickProgress = 0;
        }
        else {
            tickProgress++;
        }
        return tickProgress;
    }
}
