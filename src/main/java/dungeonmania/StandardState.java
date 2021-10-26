package dungeonmania;

public class StandardState implements GameState{
    public int spawnZombie(int tickProgress) {
        if (tickProgress == 20) {
            // SPAWN ZOMBIE
            tickProgress = 0;
        }
        else {
            tickProgress++;
        }
        return tickProgress;
    }
}
