package dungeonmania.gamemap;

public class PeacefulState implements GameState{
    String mode = "Peaceful";
    public int spawnZombie(int tickProgress) {
        return 0;
    }

    public String getMode() {
        return mode;
    }
}
