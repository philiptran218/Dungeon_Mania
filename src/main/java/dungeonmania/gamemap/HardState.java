package dungeonmania.gamemap;

public class HardState implements GameState {
    String mode = "Hard";
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

    public String getMode() {
        return mode;
    }
}
