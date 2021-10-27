package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class TheOneRing extends CombatItems {
    
    private Player player;
    /**
     * Constructor for TheOneRing
     * @param id
     * @param type
     * @param pos
     */
    public TheOneRing(String id, String type, Position pos, Player player) {
        super(id, type, pos);
        this.setPlayer(player);
    }

    // Getters and Setters
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Revives the player if their health falls to 0. Removing TheOneRing from
     * inventory should be done separately.
     */
    public void revive() {
        player.setHealth(100);
    }

}