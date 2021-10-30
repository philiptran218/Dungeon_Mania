package dungeonmania.gamemap;

public class StandardState implements GameState{
    private String mode = "Standard";
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

    public String getMode() {
        return mode;
    }
}
