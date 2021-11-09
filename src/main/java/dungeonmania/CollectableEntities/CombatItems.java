package dungeonmania.CollectableEntities;

import org.json.JSONObject;

import dungeonmania.util.Position;

public abstract class CombatItems extends CollectableEntity {

    private int durability;
    /**
     * Constructor for CombatItems
     * @param id
     * @param type
     * @param pos
     */
    public CombatItems(String id, String type, Position pos) {
        super(id, type, pos);
    }
  
    // Override the function toJSONObject in entity
    @Override
    public JSONObject toJSONObject() {
         JSONObject obj = super.toJSONObject();
         obj.put("durability", getDurability());
         return obj;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int num) {
        this.durability = num;
    }
}