package dungeonmania;

import dungeonmania.MovingEntities.Player;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.CollectableEntities.CollectableEntity;

public class Inventory {
    
    private Player player;
    private Map<String, CollectableEntity> entities = new HashMap<>(); 

    /**
     * Constructor for Inventory
     * @param player
     */
    public Inventory(Player player) {
        this.setPlayer(player);
    }

    // Getters and Setters
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, CollectableEntity> getInventory() {
        return entities;
    }

    // Adding items is done via the separate classes, so that switch
    // cases are not required.

    public CollectableEntity getEntity(String id) {
        return this.getInventory().get(id);
    }

}
