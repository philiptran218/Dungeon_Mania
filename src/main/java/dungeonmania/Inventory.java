package dungeonmania;

import dungeonmania.MovingEntities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.CollectableEntities.Key;

public class Inventory {
    
    private Player player;
    private List<CollectableEntity> entities = new ArrayList<>(); 

    public void put(Entity entity){
        if (entity instanceof CollectableEntity) {
            this.entities.add((CollectableEntity) entity);
        }
    }

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

    public List<CollectableEntity> getInventory() {
        return entities;
    }

    /**
     * Gets the number of items in inventory that are the given type
     * @param type
     * @return
     */
    public int getNoItemType(String type) {
        int count = 0;
        for (CollectableEntity item : entities) {
            if (item.getType().equals(type)) {
                count++;
            }
        }
        return count;
    }

    public CollectableEntity getItem(String type) {
        for (CollectableEntity item : entities) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns the key with the given keyId (separate from getItem since keys
     * have unique integer id's)
     * @param keyId
     * @return
     */
    public Key getKey(int keyId) {
        for (CollectableEntity item : entities) {
            if (item.getType().equals("key")) {
                Key itm = (Key) item;
                if (itm.getKeyId() == keyId) {
                    return itm;
                }
            }
        }
        return null;
    }

    /**
     * Uses the item with the given id.
     * @param id
     */
    public void useItem(String type) {
        getItem(type).use();
    }

}
