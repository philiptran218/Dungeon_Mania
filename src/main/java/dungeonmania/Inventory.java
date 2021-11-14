package dungeonmania;

import dungeonmania.MovingEntities.Player;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.CollectableEntities.*;

public class Inventory {
    
    private Player player;
    private List<CollectableEntity> entities = new ArrayList<>(); 

    public void put(Entity entity, Player player){
        if (entity instanceof CollectableEntity) {
            CollectableEntity ent = (CollectableEntity) entity;
            ent.setPlayer(player);
            this.entities.add(ent);
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

    public CollectableEntity getItemById(String id) {
        for (CollectableEntity item : entities) {
            if (item.getId().equals(id)) {
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

    /**
     * Converts the inventory to a JSONObject
     * @return
     */
    public JSONArray toJSON() {
        JSONArray jsonArray = new JSONArray();
        // Add all inventory items
        for (CollectableEntity e : entities) {
            jsonArray.put(e.toJSONObjectInventory());
        }
        return jsonArray;
    }

    /**
     * Checks if the player has enough materials to build the given buildable
     * @param buildable
     * @return
     */
    public boolean hasEnoughMaterials(String buildable) {
        switch (buildable) {
            case "bow":
                if (getNoItemType("wood") < 1 || getNoItemType("arrow") < 3) {
                    return false;
                }
                return true;    
            case "shield":
                if (getNoItemType("wood") < 2 || (getNoItemType("treasure") < 1 && getNoItemType("key") < 1 && getNoItemType("sun_stone") < 1)) {
                return false;
                }
                return true;
            case "sceptre":
                if ((getNoItemType("wood") < 1 && getNoItemType("arrow") < 2) || (getNoItemType("treasure") < 1 && 
                     getNoItemType("key") < 1 && getNoItemType("sun_stone") < 2) || getNoItemType("sun_stone") < 1) {
                    return false;
                }
                return true;
            case "midnight_armour":
                if (getNoItemType("armour") < 1 || getNoItemType("sun_stone") < 1) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void buildItem(String buildable) {
        switch (buildable) {
            case "bow":
                useItem("wood");
                for (int i = 0; i < 3; i++) {
                    useItem("arrow");
                }
                Bow newBow = new Bow("" + System.currentTimeMillis(), "bow", null);
                put(newBow, player);
                break;
            case "shield":
                useItem("wood");
                useItem("wood");
                if (getNoItemType("treasure") >= 1) {
                    useItem("treasure");
                } else if (getNoItemType("key") >= 1) {
                    useItem("key");
                } else {
                    useItem("sun_stone");
                }
                Shield newShield = new Shield("" + System.currentTimeMillis(), "shield", null);
                put(newShield, player);
                break;
            case "sceptre":
                if (getNoItemType("wood") >= 1) {
                    useItem("wood");
                } else {
                    useItem("arrow");
                    useItem("arrow");
                }
                if (getNoItemType("treasure") >= 1) {
                    useItem("treasure");
                } else if (getNoItemType("key") >= 1) {
                    useItem("key");
                } else {
                    useItem("sun_stone");
                }
                useItem("sun_stone");
                Sceptre newSceptre = new Sceptre("" + System.currentTimeMillis(), "sceptre", null);
                put(newSceptre, player);
                break;
            case "midnight_armour":
                useItem("armour");
                useItem("sun_stone");
                MidnightArmour newMidnightArmour = new MidnightArmour("" + System.currentTimeMillis(), "midnight_armour", null);
                put(newMidnightArmour, player);
                break;
        }
    }
}
